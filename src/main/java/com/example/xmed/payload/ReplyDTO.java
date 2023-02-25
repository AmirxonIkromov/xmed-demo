package com.example.xmed.payload;

import jakarta.validation.constraints.NotNull;

public record ReplyDTO(@NotNull Long replierId, String content, @NotNull Long messageId) {
}
