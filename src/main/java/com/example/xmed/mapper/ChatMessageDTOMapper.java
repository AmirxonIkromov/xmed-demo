package com.example.xmed.mapper;

import com.example.xmed.entity.ChatMessage;
import com.example.xmed.payload.ChatMessageDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ChatMessageDTOMapper implements Function<ChatMessage, ChatMessageDTO> {

    @Override
    public ChatMessageDTO apply(ChatMessage chatMessage) {
        return new ChatMessageDTO(
                chatMessage.getId(),
                chatMessage.getReplyId(),
                chatMessage.getRoomId(),
                chatMessage.getSender().getId(),
                chatMessage.getSender().getFullName(),
                chatMessage.getRecipient().getId(),
                chatMessage.getRecipient().getFullName(),
                chatMessage.getContent(),
                null);

    }
}
