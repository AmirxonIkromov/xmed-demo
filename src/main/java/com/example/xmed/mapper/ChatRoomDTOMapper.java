package com.example.xmed.mapper;

import com.example.xmed.entity.ChatRoom;
import com.example.xmed.payload.ChatRoomDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ChatRoomDTOMapper implements Function<ChatRoom, ChatRoomDTO> {
    @Override
    public ChatRoomDTO apply(ChatRoom chatRoom) {
        return new ChatRoomDTO(
                chatRoom.getId(),
                chatRoom.getSender().getId(),
                chatRoom.getRecipient().getId(),
                chatRoom.getDateTime()
        );
    }
}
