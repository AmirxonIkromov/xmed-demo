package com.example.xmed.payload;

public record ChatRoomDTO(
        Long id,
        Long senderId,
        Long recipientId,
        String dateTime
) {
}
