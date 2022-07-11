package com.example.onlinechat.service;

import com.example.onlinechat.exception.ForbiddenException;
import com.example.onlinechat.model.GroupChat;
import com.example.onlinechat.model.User;
import com.example.onlinechat.repository.GroupChatRepository;
import com.example.onlinechat.repository.UserGroupChatRepository;
import com.example.onlinechat.service.dto.FileLocationDTO;
import com.example.onlinechat.service.dto.GroupChatWithLastMessageDTO;
import com.example.onlinechat.service.dto.GroupChatWithMembersAndMessagesDTO;
import com.example.onlinechat.service.dto.MessageDTO;
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

    public GroupChat getByIdOrThrow(UUID id) {
        return groupChatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Group chat not found"));
    }

    public void confirmAccessOrThrow(UUID groupChatId, String username) {
        if (!userGroupChatRepository.existsByUserUsernameAndGroupChatId(username, groupChatId))
            throw new ForbiddenException();
    }

    public List<GroupChatWithLastMessageDTO> findGroupChatsWithLastMessageByMemberUsername(String username) {
        return groupChatRepository.findGroupChatsWithLastMessageByMemberUsername(username);
    }

    public GroupChatWithMembersAndMessagesDTO getGroupChatWithMembersAndMessagesById(UUID groupChatId, String username) {
        final GroupChat groupChat = getByIdOrThrow(groupChatId);

        final Set<User> members = groupChat.getMembers();
        members.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(ForbiddenException::new);

        final List<MessageDTO> messages = messageService.findAllByGroupChatIdOrderBySentOn(groupChatId);

        return new GroupChatWithMembersAndMessagesDTO(groupChat, members, messages);
    }

    public MessageDTO saveNewMessage(String username, UUID groupChatId, String message) {
        UUID userId = groupChatRepository.getUserIdIfMember(groupChatId.toString(), username)
                .map(UUID::fromString)
                .orElseThrow(ForbiddenException::new);

        return messageService.save(groupChatId, userId, message);
    }

    public FileLocationDTO updateProfilePhoto(
            UUID groupChatId,
            String username,
            byte[] photo,
            String originalFilename
    ) throws Exception {
        final String extension = Util.getFileExtension(originalFilename);

        confirmAccessOrThrow(groupChatId, username);
        final GroupChat groupChat = getByIdOrThrow(groupChatId);

        final String previousLocation = groupChat.getProfilePhotoLocation();
        if (previousLocation != null) {
            fileService.delete(previousLocation);
        }

        final String location = fileService.save(photo, extension);
        groupChat.setProfilePhotoLocation(location);
        return new FileLocationDTO(location);
    }
}
