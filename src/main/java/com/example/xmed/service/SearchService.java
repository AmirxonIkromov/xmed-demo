package com.example.xmed.service;

import com.example.xmed.entity.Doctor;
import com.example.xmed.payload.SearchDTO;
import com.example.xmed.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final DoctorRepository doctorRepository;


    public ResponseEntity<?> search(SearchDTO searchDTO) {
        if (searchDTO.text() != null)
            return ResponseEntity.ok(doctorRepository.findDoctorsByFullNameContaining(searchDTO.text()));
        if (searchDTO.categoryId() != null) {
            var doctorsByCategory = doctorRepository.findAllByCategoryId(searchDTO.categoryId());

            if (searchDTO.sortBy() != null && searchDTO.sortBy().equals("rate")){
                doctorsByCategory.sort(Comparator.comparing(Doctor::getRate).reversed());
               return ResponseEntity.ok(doctorsByCategory);
            }
            if (searchDTO.sortBy() != null && searchDTO.sortBy().equals("price")){
                doctorsByCategory.sort((Comparator.comparing(Doctor::getPricePerMin)));
                return ResponseEntity.ok(doctorsByCategory);
            }
            if (searchDTO.sortBy() != null && searchDTO.sortBy().equals("experience")){
                doctorsByCategory.sort((Comparator.comparing(Doctor::getExperience)));
                return ResponseEntity.ok(doctorsByCategory);
            }

            return ResponseEntity.ok(doctorsByCategory);
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
    }
}
