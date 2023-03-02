package com.example.xmed.payload;

public record SearchDTO(

        String text,
        Long categoryId,
        String sortBy
) {
}
