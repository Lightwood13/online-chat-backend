package com.example.onlinechat.service;

import com.example.onlinechat.exception.ForbiddenException;
import com.example.onlinechat.model.ChatMember;
import com.example.onlinechat.model.GroupChat;
import com.example.onlinechat.model.keys.UserGroupChatPrimaryKey;
import com.example.onlinechat.repository.ChatMemberRepository;
import com.example.onlinechat.repository.GroupChatRepository;
import com.example.onlinechat.repository.UserRepository;
import com.example.onlinechat.service.dto.FileLocationDTO;
import com.example.onlinechat.service.dto.GroupChatDTO;
import com.example.onlinechat.service.dto.GroupChatWithLastMessageDTO;
import com.example.onlinechat.service.dto.MessageDTO;
import com.example.onlinechat.util.Util;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class GroupChatService {

    private final GroupChatRepository groupChatRepository;
    private final ChatMemberRepository chatMemberRepository;

    private final UserRepository userRepository;
    private final FileService fileService;
    private final MessageService messageService;

    public GroupChatService(
            GroupChatRepository groupChatRepository,
            ChatMemberRepository chatMemberRepository,
            UserRepository userRepository,
            FileService fileService,
            MessageService messageService) {
        this.groupChatRepository = groupChatRepository;
        this.chatMemberRepository = chatMemberRepository;
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.messageService = messageService;
    }

    private GroupChat confirmAccessAndGetGroupChatByIdOrThrow(UUID groupChatId, UUID userId) {
        return groupChatRepository.confirmAccessAndGetById(groupChatId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group chat not found"));
    }

    private void confirmAccessOrThrow(UUID groupChatId, UUID userId) {
        if (!chatMemberRepository.existsById_UserIdAndId_GroupChatId(userId, groupChatId))
            throw new ForbiddenException();
    }

    public GroupChatDTO getByIdOrThrow(UUID groupChatId, UUID userId) {
        return GroupChatDTO.fromGroupChat(confirmAccessAndGetGroupChatByIdOrThrow(groupChatId, userId));
    }

    public List<UUID> findUserIdsThatShareGroupChatWith(UUID userId) {
        return chatMemberRepository.getUserIdsThatShareGroupChatWith(userId);
    }

    public List<GroupChatWithLastMessageDTO> findGroupChatsWithLastMessageByMemberId(UUID id) {
        return groupChatRepository.findGroupChatsWithLastMessageByMemberId(id.toString());
    }

    public List<MessageDTO> getMessages(UUID groupChatId, UUID userId) {
        confirmAccessOrThrow(groupChatId, userId);
        return messageService.findAllByGroupChatIdOrderBySentOn(groupChatId);
    }

    public MessageDTO saveNewMessage(UUID userId, UUID groupChatId, String message) {
        confirmAccessOrThrow(groupChatId, userId);
        return messageService.save(groupChatId, userId, message);
    }

    public FileLocationDTO updateProfilePhoto(
            UUID groupChatId,
            UUID userId,
            byte[] photo,
            String originalFilename
    ) throws Exception {
        final String extension = Util.getFileExtension(originalFilename);

        final GroupChat groupChat = confirmAccessAndGetGroupChatByIdOrThrow(groupChatId, userId);

        final String location = fileService.saveOrUpdate(
                photo,
                extension,
                groupChat.getProfilePhotoLocation());
        groupChat.setProfilePhotoLocation(location);
        return new FileLocationDTO(location);
    }

    public GroupChatDTO createNewGroupChat(String name, List<UUID> memberIds, UUID ownerId) {
        if (!memberIds.contains(ownerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Members should include owner");
        }
        final GroupChat savedGroupChat = groupChatRepository.save(GroupChat.builder()
                .name(name)
                .createdOn(Timestamp.from(Instant.now()))
                .build());
        final Set<ChatMember> members = StreamSupport.stream(userRepository.findAllById(memberIds).spliterator(), false)
                .map(user -> ChatMember.builder()
                        .id(new UserGroupChatPrimaryKey(user.getId(), savedGroupChat.getId()))
                        .groupChat(savedGroupChat)
                        .user(user)
                        .role(user.getId().equals(ownerId) ? ChatMember.Role.admin : ChatMember.Role.member)
                        .build())
                .collect(Collectors.toSet());
        if (members.size() != memberIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One of user ids is not found");
        }
        savedGroupChat.setMembers(members);
        return GroupChatDTO.fromGroupChat(savedGroupChat);
    }
}
