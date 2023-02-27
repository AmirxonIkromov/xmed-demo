package com.example.xmed.service;

import com.example.xmed.entity.ChatMessage;
import com.example.xmed.enums.MessageStatus;
import com.example.xmed.enums.MessageType;
import com.example.xmed.payload.ChatMessageDTO;
import com.example.xmed.payload.EditMessageDTO;
import com.example.xmed.payload.ReplyDTO;
import com.example.xmed.repository.ChatMessageRepository;
import com.example.xmed.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    public ResponseEntity<?> message(ChatMessageDTO chatMessageDTO) {
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String time = localDateTime.format(format);

            if (chatMessageDTO.getMessageType().equals(MessageType.SEND)) {
                var roomId = chatRoomService.getRoomId(chatMessageDTO.getSenderId(), chatMessageDTO.getRecipientId());

                var chatMessage = ChatMessage.builder()
                        .senderId(chatMessageDTO.getSenderId())
                        .senderName(chatMessageDTO.getSenderName())
                        .recipientId(chatMessageDTO.getRecipientId())
                        .recipientName(chatMessageDTO.getRecipientName())
                        .content(chatMessageDTO.getContent())
                        .status(MessageStatus.DELIVERED)
                        .roomId(roomId)
                        .dateTime(time)
                        .build();
                return ResponseEntity.status(HttpStatus.CREATED).body(chatMessageRepository.save(chatMessage));
            }

            if (chatMessageDTO.getMessageType().equals(MessageType.READ)) {
                var chatMessage = chatMessageRepository.findById(chatMessageDTO.getMessageId()).orElseThrow();
                var chatMessages = chatMessageRepository.
                        findAllByRoomIdAndDateTimeBefore(chatMessage.getRoomId(), chatMessage.getDateTime());
                for (ChatMessage message : chatMessages) {
                    message.setStatus(MessageStatus.READ);
                    chatMessageRepository.save(message);
                }
                return ResponseEntity.ok().build();
            }

            if (chatMessageDTO.getMessageType().equals(MessageType.REPLY)) {
                var repliedMessage = chatMessageRepository.findById(chatMessageDTO.getMessageId()).orElseThrow();
                var newMessage = ChatMessage.builder()
                        .senderId(chatMessageDTO.getSenderId())
                        .recipientId(chatMessageDTO.getRecipientId())
                        .replyId(repliedMessage.getId())
                        .content(chatMessageDTO.getContent())
                        .roomId(repliedMessage.getRoomId())
                        .dateTime(time)
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(chatMessageRepository.save(newMessage));
            }

            if (chatMessageDTO.getMessageType().equals(MessageType.PIN)) {
                var chatMessage = chatMessageRepository.findById(chatMessageDTO.getMessageId()).orElseThrow();
                chatMessage.setPined(true);
                chatMessageRepository.save(chatMessage);
                return ResponseEntity.ok().build();
            }

            if (chatMessageDTO.getMessageType().equals(MessageType.EDIT)) {

                var chatMessage = chatMessageRepository.findById(chatMessageDTO.getMessageId()).orElseThrow();
                if (chatMessage.getSenderId().equals(chatMessageDTO.getSenderId())) {
                    chatMessage.setContent(chatMessage.getContent());
                    chatMessage.setEdited(true);
                    chatMessageRepository.save(chatMessage);
                    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
                }
            }

            if (chatMessageDTO.getMessageType().equals(MessageType.DELETE)) {

                var chatMessage = chatMessageRepository.findById(chatMessageDTO.getMessageId()).orElseThrow();
                if (chatMessage.getSenderId().equals(chatMessageDTO.getSenderId())) {
                    chatMessage.setDeleted(true);
                    chatMessage.setDeletedTime(time);
                    chatMessageRepository.save(chatMessage);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                }
            }
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    public ResponseEntity<?> getPinList(Long roomId) {
        return ResponseEntity.ok(chatMessageRepository.
                findAllByRoomIdAndPinedOrderByDateTimeDesc(roomId, true));
    }

    public ResponseEntity<?> messageList(Long senderId, Long recipientId) {
        return ResponseEntity.ok(chatMessageRepository.
                findAllBySenderIdAndRecipientIdAndDeletedNotOrderByDateTimeDesc(senderId, recipientId, true));

    }
}