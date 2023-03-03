package com.example.xmed.service;

import com.example.xmed.entity.Doctor;
import com.example.xmed.mapper.DoctorDTOMapper;
import com.example.xmed.payload.SearchDTO;
import com.example.xmed.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SearchService {

    private final DoctorRepository doctorRepository;
    private final DoctorDTOMapper doctorDTOMapper;


    public ResponseEntity<?> search(SearchDTO searchDTO) {
        try {

            if (searchDTO.text() != null) {
                var doctorsByCategory = doctorRepository.findDoctorsByContaining(searchDTO.text());
                var doctors = filter(doctorsByCategory, searchDTO);
//                doctorDTOMapper.apply(doctors);
                return ResponseEntity.ok(doctors);
            }
            if (searchDTO.categoryId() != null) {
                var doctorsByCategory = doctorRepository.findAllByCategoryId(searchDTO.categoryId());

                if (searchDTO.sortBy() != null && searchDTO.sortBy().equals("rate"))
                    doctorsByCategory.sort(Comparator.comparing(Doctor::getRate).reversed());

                if (searchDTO.sortBy() != null && searchDTO.sortBy().equals("price"))
                    doctorsByCategory.sort((Comparator.comparing(Doctor::getPricePerMin)));

                if (searchDTO.sortBy() != null && searchDTO.sortBy().equals("experience"))
                    doctorsByCategory.sort((Comparator.comparing(Doctor::getExperience).reversed()));
                var doctors = filter(doctorsByCategory, searchDTO);
                return ResponseEntity.ok(doctors);
            }
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
    }

    private List<Doctor> filter(List<Doctor> doctors, SearchDTO searchDTO) {
        if (searchDTO.filterByPriceMin() != null) {
            doctors = doctors.stream()
                    .filter(p -> p.getPricePerMin() >= searchDTO.filterByPriceMin()).toList();
        }
        if (searchDTO.filterByPriceMax() != null) {
            doctors = doctors.stream()
                    .filter(p -> p.getPricePerMin() <= searchDTO.filterByPriceMax()).toList();
        }
        if (searchDTO.filterByExperience() != null) {
            doctors = doctors.stream()
                    .filter(p -> p.getExperience() >= searchDTO.filterByExperience()).toList();
        }
        if (searchDTO.filterByOnline())
            doctors.removeIf(p -> !p.isOnline());
        if (searchDTO.filterByOffline())
            doctors.removeIf(Doctor::isOnline);

        if (searchDTO.filterByRate() != null) {
            doctors = doctors.stream()
                    .filter(p -> p.getRate() >= searchDTO.filterByRate())
                    .toList();
        }
        return doctors;
    }
}
