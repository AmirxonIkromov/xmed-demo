package com.example.xmed.service;

import com.example.xmed.entity.ChatRoom;
import com.example.xmed.entity.User;
import com.example.xmed.repository.ChatRoomRepository;
import com.example.xmed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public Long getRoomId(Long senderId, Long recipientId) {
        var sender = userRepository.findById(senderId).orElseThrow();
        var recipient = userRepository.findById(recipientId).orElseThrow();
        var chatRoom = chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId).or(
                () -> chatRoomRepository.findBySenderIdAndRecipientId(recipientId, senderId)
        ).orElse(
                ChatRoom.builder()
                        .sender(sender)
                        .recipient(recipient)
                        .build());

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String time = localDateTime.format(format);

        chatRoom.setDateTime(time);
        var saved = chatRoomRepository.save(chatRoom);
        return saved.getId();
    }

    public ResponseEntity<?> chatList(Long senderId) {
        return ResponseEntity.ok(chatRoomRepository
                .findAllBySenderIdOrRecipientIdOrderByDateTimeDesc(senderId, senderId));
    }
}