package com.example.xmed.controller;

import com.example.xmed.entity.ChatNotification;
import com.example.xmed.payload.ChatMessageDTO;
import com.example.xmed.service.ChatMessageService;
import com.example.xmed.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/message")
    public ResponseEntity<?> message(@Payload ChatMessageDTO chatMessageDTO) {
        if (chatMessageDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String toId = String.valueOf(chatMessageDTO.getRecipientId());
        String fromId = String.valueOf(chatMessageDTO.getSenderId());

        simpMessagingTemplate.convertAndSendToUser(
                toId, "/topic/message-list/" + fromId + "/" + toId,
                new ChatNotification(
                        chatMessageDTO.getSenderId(),
                        chatMessageDTO.getSenderName()));

        simpMessagingTemplate.convertAndSendToUser(
                fromId, "/topic/message-list/" + toId + "/" + fromId,
                new ChatNotification(
                        chatMessageDTO.getSenderId(),
                        chatMessageDTO.getSenderName()));

        return chatMessageService.message(chatMessageDTO);

    }

    @GetMapping("/chat-list")
    public ResponseEntity<?> chatList(@RequestParam Long senderId) {
        if (senderId == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        ResponseEntity<?> chatList = chatRoomService.chatList(senderId);
        String toId = String.valueOf(senderId);
        simpMessagingTemplate.convertAndSend("/topic/chat-list/" + toId, chatList);
        return chatList;
    }

    @GetMapping("/message-list")
    public ResponseEntity<?> messageList(@RequestParam Long roomId) {
        if (roomId == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        ResponseEntity<?> messageList = chatMessageService.messageList(roomId);
        simpMessagingTemplate.convertAndSend("/topic/message", messageList);
        return messageList;
    }

    @GetMapping("/pin-list")
    public ResponseEntity<?> pinList(@RequestParam Long roomId) {
        if (roomId == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        ResponseEntity<?> pinList = chatMessageService.pinList(roomId);
        simpMessagingTemplate.convertAndSend("/topic/message", pinList);
        return pinList;
    }
}