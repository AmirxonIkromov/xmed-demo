package com.example.xmed.service;

import com.example.xmed.entity.ChatMessage;
import com.example.xmed.entity.ChatRoom;
import com.example.xmed.enums.MessageStatus;
import com.example.xmed.repository.ChatMessageRepository;
import com.example.xmed.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessage save(ChatMessage chatMessage) {
//        chatMessage.setStatus(MessageStatus.RECEIVED);
        chatMessage.setStatus(chatMessage.getStatus());
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(Long senderId, Long recipientId) {
        return chatMessageRepository.countBySenderIdAndRecipientIdAndStatus(
                senderId, recipientId, MessageStatus.RECEIVED);
    }

    public List<ChatMessage> findChatMessages(Long senderId, Long recipientId) {
        var chatId = chatRoomService.getChatId(senderId, recipientId, false);

        var messages = chatId.map(chatMessageRepository::findByChatId).orElse(new ArrayList<>());

        if (messages.size() > 0) {
//            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
            updateStatuses(senderId, recipientId, "DELIVERED" );
        }

        return messages;
    }

    public ChatMessage findById(Long id) {
        return chatMessageRepository
                .findById(id)
                .map(chatMessage -> {
//                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    chatMessage.setStatus(chatMessage.getStatus());
                    return chatMessageRepository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("can't find message (" + id + ")"));
    }

    public void updateStatuses(Long senderId, Long recipientId, String  status) {
        var chatMessage = chatMessageRepository.findBySenderIdAndRecipientId(senderId, recipientId).orElseThrow();
        chatMessage.setStatus(status);
        chatMessageRepository.save(chatMessage);
    }

}