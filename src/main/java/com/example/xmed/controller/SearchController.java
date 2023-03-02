package com.example.xmed.controller;

import com.example.xmed.payload.SearchDTO;
import com.example.xmed.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<?> search(@RequestBody SearchDTO searchDTO) {
        if (searchDTO == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return searchService.search(searchDTO);
    }
}
