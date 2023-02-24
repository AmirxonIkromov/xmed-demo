package com.example.xmed.entity;

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
   private Long chatId;
   private Long senderId;
   private Long recipientId;
   private String senderName;
   private String recipientName;
   private String content;
   private boolean deleted;
   private LocalDateTime dateTime;
//   @Enumerated(EnumType.STRING)
   private String status;
}