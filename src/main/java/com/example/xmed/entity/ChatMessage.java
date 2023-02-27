package com.example.xmed.entity;

import com.example.xmed.enums.MessageStatus;
import com.example.xmed.enums.MessageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   private Long roomId;
   private Long senderId;
   private Long recipientId;
   private Long replyId;
   private String senderName;
   private String recipientName;
   private String content;
   private String  dateTime;
   private String deletedTime;
   private boolean deleted;
   private boolean pined;
   private boolean edited;
   @Enumerated(EnumType.STRING)
   private MessageStatus status;
}