package com.example.xmed.controller;

import com.example.xmed.entity.ChatMessage;
import com.example.xmed.entity.ChatNotification;
import com.example.xmed.entity.ChatRoom;
import com.example.xmed.enums.MessageStatus;
import com.example.xmed.payload.EditMessageDTO;
import com.example.xmed.payload.ReplyDTO;
import com.example.xmed.repository.ChatRoomRepository;
import com.example.xmed.service.ChatMessageService;
import com.example.xmed.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app")
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

//    @MessageMapping("/chat")
//    @SendTo("/room")
    @PostMapping("/chat")
    public void processMessage(@RequestBody ChatMessage chatMessage) {
        var chatId = chatRoomService
                .getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId.orElseThrow());
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String time = localDateTime.format(format);
        var chatRooms = chatRoomRepository.findAllByChatId(chatId.orElseThrow());

        for (ChatRoom chatRoom : chatRooms) {
            chatRoom.setDateTime(time);
            chatRoomRepository.save(chatRoom);
        }

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
    public ResponseEntity<?> chatList(@RequestParam Long senderId) {
        return ResponseEntity.ok(chatRoomService.chatList(senderId));
    }

    @GetMapping("/messageList")
    public ResponseEntity<?> messageList(@RequestParam Long senderId, Long recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @PutMapping("/editMessage")
    public ResponseEntity<?> editMessage(@RequestBody EditMessageDTO editMessageDTO) {
        return ResponseEntity.ok(chatMessageService.editMessage(editMessageDTO));
    }

    @PostMapping("/reply")
    public void reply(@RequestBody ReplyDTO replyDTO) {
        var chatMessage = chatMessageService.reply(replyDTO);
        var saved = chatMessageService.save(chatMessage);
        String recipientId = String.valueOf(chatMessage.getRecipientId());

        simpMessagingTemplate.convertAndSendToUser(
                recipientId, "/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId(),
                        saved.getSenderName()));
    }

    @PostMapping("/read")
    public void read(@RequestParam Long senderId, Long recipientId) {
        chatMessageService.updateStatuses(senderId, recipientId, MessageStatus.READ.name());
    }

    @PostMapping("/pin")
    public void pin(@RequestParam Long messageId){
        chatMessageService.updateStatuses(messageId);
    }

    @GetMapping("/pinList/{chatId}")
    public ResponseEntity<?> getPinList(@PathVariable Long chatId){
        return ResponseEntity.ok(chatMessageService.getPinList(chatId));
    }
}