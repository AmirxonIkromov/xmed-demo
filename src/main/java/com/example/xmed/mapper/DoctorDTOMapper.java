package com.example.xmed.mapper;

import com.example.xmed.entity.Doctor;
import com.example.xmed.payload.DoctorDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DoctorDTOMapper implements Function<Doctor, DoctorDTO> {

    @Override
    public DoctorDTO apply(Doctor doctor) {
        return new DoctorDTO(
                doctor.getId(),
                doctor.getRate(),
                doctor.getAbout(),
                doctor.getEducation(),
                doctor.getExperience(),
                doctor.getPricePerMin(),
                doctor.getWorkplace(),
                doctor.getCategory().getId(),
                doctor.getUser().getId(),
                doctor.getUser().getFullName()
        );
    }
}
