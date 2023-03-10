package com.example.xmed.payload;

import com.example.xmed.entity.Category;

import java.util.List;

public record DoctorDTO (
        Long id,
        Float rate,
        String about,
        String education,
        Integer experience,
        Integer pricePerMin,
        String workplace,
        boolean online,
        Long userId,
        String fullName,
        Long categoryId,
        String categoryName,
        String categorySlag

) {
}
