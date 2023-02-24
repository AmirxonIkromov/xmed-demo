package com.example.xmed.payload;

public record EditMessageDTO(Long senderId, Long messageId, String newContent) {
}
