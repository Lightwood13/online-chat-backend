package com.example.onlinechat.service;

import com.example.onlinechat.exception.ForbiddenException;
import com.example.onlinechat.model.GroupChat;
import com.example.onlinechat.model.User;
import com.example.onlinechat.repository.GroupChatRepository;
import com.example.onlinechat.repository.UserGroupChatRepository;
import com.example.onlinechat.service.dto.*;
import com.example.onlinechat.util.Util;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class GroupChatService {

    private final GroupChatRepository groupChatRepository;
    private final UserGroupChatRepository userGroupChatRepository;
    private final FileService fileService;
    private final MessageService messageService;

    public GroupChatService(
            GroupChatRepository groupChatRepository,
            UserGroupChatRepository userGroupChatRepository,
            FileService fileService,
            MessageService messageService) {
        this.groupChatRepository = groupChatRepository;
        this.userGroupChatRepository = userGroupChatRepository;
        this.fileService = fileService;
        this.messageService = messageService;
    }

    private GroupChat getGroupChatByIdOrThrow(UUID id) {
        return groupChatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Group chat not found"));
    }

    public GroupChatDTO getByIdOrThrow(UUID id) {
        return GroupChatDTO.fromGroupChat(getGroupChatByIdOrThrow(id));
    }

    private void confirmAccessOrThrow(UUID groupChatId, UUID userId) {
        if (!userGroupChatRepository.existsByUserIdAndGroupChatId(userId, groupChatId))
            throw new ForbiddenException();
    }

    public List<UUID> findUserIdsThatShareGroupChatWith(UUID userId) {
        return userGroupChatRepository.getUserIdsThatShareGroupChatWith(userId.toString())
                .stream()
                .map(UUID::fromString)
                .toList();
    }

    public List<GroupChatWithLastMessageDTO> findGroupChatsWithLastMessageByMemberId(UUID id) {
        return groupChatRepository.findGroupChatsWithLastMessageByMemberId(id.toString());
    }

    public GroupChatWithMembersAndMessagesDTO getGroupChatWithMembersAndMessagesById(UUID groupChatId, UUID userId) {
        final GroupChat groupChat = getGroupChatByIdOrThrow(groupChatId);

        final Set<User> members = groupChat.getMembers();
        members.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(ForbiddenException::new);

        final List<MessageDTO> messages = messageService.findAllByGroupChatIdOrderBySentOn(groupChatId);

        return new GroupChatWithMembersAndMessagesDTO(groupChat, members, messages);
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

        confirmAccessOrThrow(groupChatId, userId);
        final GroupChat groupChat = getGroupChatByIdOrThrow(groupChatId);

        final String location = fileService.saveOrUpdate(
                photo,
                extension,
                groupChat.getProfilePhotoLocation());
        groupChat.setProfilePhotoLocation(location);
        return new FileLocationDTO(location);
    }
}
