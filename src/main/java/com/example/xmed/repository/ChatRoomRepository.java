package com.example.xmed.repository;

import com.example.xmed.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(Long senderId, Long recipientId);

    List<ChatRoom> findAllBySenderIdOrRecipientIdOrderByDateTimeDesc(Long senderId, Long recipientId);

}