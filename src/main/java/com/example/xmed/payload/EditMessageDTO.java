package com.example.xmed.payload;

import com.example.xmed.enums.MessageStatus;

public record EditMessageDTO(Long senderId, Long messageId, String newContent, String action) {
}
