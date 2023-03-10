package com.example.xmed.service;

import com.example.xmed.entity.Category;
import com.example.xmed.mapper.DoctorDTOMapper;
import com.example.xmed.payload.SearchDTO;
import com.example.xmed.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SearchService {

    private final DoctorRepository doctorRepository;
    private final DoctorDTOMapper doctorDTOMapper;

//    public ResponseEntity<?> search(SearchDTO searchDTO) {
//        try {
//            if (searchDTO.text() != null) {
//                var doctorsByCategory = doctorRepository.findDoctorsByContaining(searchDTO.text());
//                var doctors = filter(doctorsByCategory, searchDTO);
//                var doctorDTOList = doctors.stream().map(doctorDTOMapper).collect(Collectors.toList());
//                return ResponseEntity.ok(doctorDTOList);
//            }
//            if (searchDTO.categoryId() != null) {
//                var doctorsByCategory = doctorRepository.findAllByCategoryId(
//                        searchDTO.categoryId(), Sort.by(Sort.Direction.DESC, searchDTO.sortBy()));

//                var doctorsByCategory = doctorRepository.findAllByCategoryId(
//                        searchDTO.categoryId(), PageRequest.of(2, 3).withSort(Sort.by(searchDTO.sortBy())));

//                var doctors = filter(doctorsByCategory, searchDTO);
//                var doctorDTOList = doctors.stream().map(doctorDTOMapper).collect(Collectors.toList());
//                return ResponseEntity.ok(doctorDTOList);
//            }
//        }catch (Exception ex){
//            ex.getStackTrace();
//        }
//        return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
//    }
//
//        private List<Doctor> filter(List<Doctor> doctors, SearchDTO searchDTO) {
//        if (searchDTO.filterByPriceMin() != null) {
//            doctors = doctors.stream()
//                    .filter(p -> p.getPricePerMin() >= searchDTO.filterByPriceMin()).toList();
//        }
//        if (searchDTO.filterByPriceMax() != null) {
//            doctors = doctors.stream()
//                    .filter(p -> p.getPricePerMin() <= searchDTO.filterByPriceMax()).toList();
//        }
//        if (searchDTO.filterByExperience() != null) {
//            doctors = doctors.stream()
//                    .filter(p -> p.getExperience() >= searchDTO.filterByExperience()).toList();
//        }
//        if (searchDTO.filterByOnline())
//            doctors.removeIf(p -> !p.isOnline());
//
//        if (searchDTO.filterByOffline())
//            doctors.removeIf(Doctor::isOnline);
//
//        if (searchDTO.filterByRate() != null) {
//            doctors = doctors.stream()
//                    .filter(p -> p.getRate() >= searchDTO.filterByRate())
//                    .toList();
//        }
//        return doctors;
//    }
    public ResponseEntity<?> search(SearchDTO searchDTO) {
        try {

            Long categoryId = searchDTO.categoryId();
            String text = searchDTO.text() == null ? "" : searchDTO.text();
            String sortBy = searchDTO.sortBy() == null ? "" : searchDTO.sortBy();
            Integer filterByPriceMin = searchDTO.filterByPriceMin() == null ? 0 : searchDTO.filterByPriceMin();
            Integer filterByPriceMax = searchDTO.filterByPriceMax() == null ? 100000 : searchDTO.filterByPriceMax();
            Integer filterByExperience = searchDTO.filterByExperience() == null ? 0 : searchDTO.filterByExperience();
            Float filterByRate = searchDTO.filterByRate() == null ? 0 : searchDTO.filterByRate();
            Boolean filterByOnline = searchDTO.filterByOnline();
            Boolean filterByOffline = searchDTO.filterByOffline();

            if (categoryId != null) {
                return ResponseEntity.ok(doctorRepository.findDoctorsByCategory(categoryId, filterByPriceMin, filterByPriceMax,
                                filterByExperience, filterByRate, sortBy, filterByOffline, filterByOnline)
                        .stream().map(doctorDTOMapper).collect(Collectors.toList()));
            }

            return ResponseEntity.ok(doctorRepository.findDoctors(text, filterByPriceMin, filterByPriceMax,
                            filterByExperience, filterByRate, sortBy, filterByOffline, filterByOnline)
                    .stream().map(doctorDTOMapper).collect(Collectors.toList()));


        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    public ResponseEntity<?> sortCategory() {
        return ResponseEntity.ok(doctorRepository.searchDoctorByCategory());
    }
}
