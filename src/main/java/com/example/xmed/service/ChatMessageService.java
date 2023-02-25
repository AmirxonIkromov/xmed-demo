package com.example.xmed.service;

import com.example.xmed.entity.ChatMessage;
import com.example.xmed.enums.MessageStatus;
import com.example.xmed.payload.DeleteMessageDTO;
import com.example.xmed.payload.EditMessageDTO;
import com.example.xmed.payload.ReplyDTO;
import com.example.xmed.repository.ChatMessageRepository;
import com.example.xmed.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED.name());
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(Long senderId, Long recipientId) {
        return chatMessageRepository.countBySenderIdAndRecipientIdAndStatus(
                senderId, recipientId, MessageStatus.RECEIVED);
    }

    public List<ChatMessage> findChatMessages(Long senderId, Long recipientId) {
        var chatId = chatRoomService.getChatId(senderId, recipientId, false).orElseThrow();

        var messages = chatMessageRepository.findALLByChatIdAndDeleted(chatId, false);

        if (messages.size() > 0) {
            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED.name());
        }

        return messages;
    }

    public ChatMessage findById(Long id) {
        return chatMessageRepository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED.name());
                    return chatMessageRepository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("can't find message (" + id + ")"));
    }

    public void updateStatuses(Long senderId, Long recipientId, String status) {
        var chatMessages = chatMessageRepository
                .findBySenderIdAndRecipientIdOrderByDateTimeDesc(senderId, recipientId);

        for (ChatMessage chatMessage : chatMessages) {
            chatMessage.setStatus(status);
            chatMessageRepository.save(chatMessage);
        }
    }

    public void updateStatuses(Long messageId) {
        var chatMessage = chatMessageRepository.findById(messageId).orElseThrow();
        chatMessage.setStatus(MessageStatus.PINED.name());
        chatMessageRepository.save(chatMessage);
    }

    public ResponseEntity<?> editMessage(EditMessageDTO editMessageDTO) {

        if (editMessageDTO.action().equals("EDIT")) {

            var chatMessage = chatMessageRepository.findById(editMessageDTO.messageId()).orElseThrow();
            if (chatMessage.getSenderId().equals(editMessageDTO.senderId())) {
                chatMessage.setContent(editMessageDTO.newContent());
                chatMessage.setStatus(MessageStatus.EDITED.name());
                chatMessageRepository.save(chatMessage);
                return ResponseEntity.status(HttpStatus.ACCEPTED).build();
            }
        }
        if (editMessageDTO.action().equals("DELETE")) {
            var chatMessage = chatMessageRepository.findById(editMessageDTO.messageId()).orElseThrow();
            if (chatMessage.getSenderId().equals(editMessageDTO.senderId())) {
                chatMessage.setDeleted(true);
                chatMessageRepository.save(chatMessage);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    public ChatMessage reply(ReplyDTO replyDTO) {
        var repliedMessage = chatMessageRepository.findById(replyDTO.messageId()).orElseThrow();
        var message = ChatMessage.builder()
                .chatId(repliedMessage.getChatId())
                .senderId(replyDTO.replierId())
                .content(replyDTO.content())
                .build();
        message.setRecipientId(repliedMessage.getSenderId().equals(replyDTO.replierId())
                ? repliedMessage.getRecipientId() : repliedMessage.getSenderId());
        repliedMessage.setStatus(MessageStatus.REPLIED.name());
        chatMessageRepository.save(repliedMessage);
        chatMessageRepository.save(message);
        return message;
    }

    public List<ChatMessage> getPinList(Long chatId) {
        return chatMessageRepository.findAllByChatIdAndStatus(chatId, MessageStatus.PINED.name());
    }
}