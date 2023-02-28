package com.example.xmed.controller;

import com.example.xmed.payload.ChatMessageDTO;
import com.example.xmed.service.ChatMessageService;
import com.example.xmed.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app")
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

//    @MessageMapping("/chat")
//    @SendTo("/room")
    @PostMapping("/message")
    public ResponseEntity<?> message(@RequestBody ChatMessageDTO chatMessageDTO) {
        if(chatMessageDTO == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return chatMessageService.message(chatMessageDTO);

//        String recipientId = String.valueOf(chatMessageDTO.getRecipientId());
//        simpMessagingTemplate.convertAndSendToUser(
//                recipientId, "/queue/messages",
//                new ChatNotification(
//                        chatMessageDTO.getSenderId(),
//                        chatMessageDTO.getSenderName()));
    }

    @GetMapping("/chat-list")
    public ResponseEntity<?> chatList(@RequestParam Long senderId) {
        if(senderId == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return chatRoomService.chatList(senderId);
    }

    @GetMapping("/message-list")
    public ResponseEntity<?> messageList(@RequestParam Long roomId) {
        if(roomId == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return chatMessageService.messageList(roomId);
    }

    @GetMapping("/pin-list")
    public ResponseEntity<?> getPinList(@RequestParam Long roomId){
        if(roomId == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return chatMessageService.getPinList(roomId);
    }
}