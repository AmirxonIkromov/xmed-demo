package com.example.xmed.mapper;

import com.example.xmed.entity.ChatMessage;
import com.example.xmed.payload.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ChatMessageDTOMapper implements Function<ChatMessage, ChatMessageDTO> {

    @Override
    public ChatMessageDTO apply(ChatMessage chatMessage) {
        return new ChatMessageDTO(
                chatMessage.getId(),
                chatMessage.getRoomId(),
                chatMessage.getSender().getId(),
                chatMessage.getSender().getFirstName(),
                chatMessage.getRecipient().getId(),
                chatMessage.getRecipient().getFirstName(),
                chatMessage.getContent(),
                null);

    }
}
