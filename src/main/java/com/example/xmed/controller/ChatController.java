package com.example.xmed.controller;

import com.example.xmed.entity.ChatMessage;
import com.example.xmed.entity.ChatNotification;
import com.example.xmed.service.ChatMessageService;
import com.example.xmed.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
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
    @PostMapping("/chat")
    public void processMessage(@RequestBody ChatMessage chatMessage) {
        var chatId = chatRoomService
                .getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId.orElseThrow());

        ChatMessage saved = chatMessageService.save(chatMessage);
        String recipientId = String.valueOf(chatMessage.getRecipientId());

        simpMessagingTemplate.convertAndSendToUser(
                recipientId, "/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId(),
                        saved.getSenderName()));
    }

    @GetMapping("/chatList")
    public ResponseEntity<?> chatList(@RequestParam Long senderId){
        return ResponseEntity.ok(chatRoomService.chatList(senderId));
    }
    
    @GetMapping("/messageList")
    public ResponseEntity<?> messageList(@RequestParam Long senderId, Long recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}