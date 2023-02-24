package com.example.xmed.repository;

import com.example.xmed.entity.ChatMessage;
import com.example.xmed.enums.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Long countBySenderIdAndRecipientIdAndStatus(Long senderId, Long recipientId, MessageStatus status);

    List<ChatMessage> findByChatIdAndDeletedNot(Long chatId, boolean deleted);

    List<ChatMessage> findBySenderIdAndRecipientIdOrderByDateTimeDesc(Long senderId, Long recipientId);
}