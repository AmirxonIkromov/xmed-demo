package com.example.xmed.payload;

public record DoctorDTO(
        Long id,
        Float rate,
        String about,
        String education,
        Integer experience,
        Integer pricePerMin,
        String workplace,
        Long categoryId,
        Long userId,
        String fullName

) {
}
