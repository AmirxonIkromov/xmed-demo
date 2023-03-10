package com.example.xmed.controller;

import com.example.xmed.payload.SearchDTO;
import com.example.xmed.repository.DoctorRepository;
import com.example.xmed.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchDTO searchDTO) {
        if (searchDTO == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return searchService.search(searchDTO);
    }

    @GetMapping("/sort-category")
    public ResponseEntity<?> sortCategory() {
        return searchService.sortCategory();
    }
}
