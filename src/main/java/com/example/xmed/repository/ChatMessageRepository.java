package com.example.xmed.repository;

import com.example.xmed.entity.ChatMessage;
import com.example.xmed.enums.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findAllByRoomIdAndDeletedNotOrderByDateTimeDesc(Long roomId, boolean deleted);

    List<ChatMessage> findAllByRoomIdAndPinedOrderByDateTimeDesc(Long roomId, boolean pined);

    List<ChatMessage> findAllByRoomIdAndDateTimeBefore(Long roomId, String dateTime);
}