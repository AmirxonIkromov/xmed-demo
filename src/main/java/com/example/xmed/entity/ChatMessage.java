package com.example.xmed.entity;

import com.example.xmed.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
   private Long replyId;
   private String content;
   private String  dateTime;
   private String deletedTime;
   private boolean deleted;
   private boolean pined;
   private boolean edited;

   @ManyToOne
   private User sender;

   @ManyToOne
   private User recipient;

   @Enumerated(EnumType.STRING)
   private MessageStatus status;


}