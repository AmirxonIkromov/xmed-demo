package com.example.xmed.payload;

public record SearchDTO(

        String text,
        Long categoryId,
        String sortBy,
        Integer filterByPriceMin,
        Integer filterByPriceMax,
        Integer filterByExperience,
        Float filterByRate,
        boolean filterByOnline,
        boolean filterByOffline
) {
}
