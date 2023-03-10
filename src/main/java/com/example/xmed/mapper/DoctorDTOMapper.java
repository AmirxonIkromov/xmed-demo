package com.example.xmed.mapper;

import com.example.xmed.entity.Category;
import com.example.xmed.entity.Doctor;
import com.example.xmed.entity.User;
import com.example.xmed.payload.DoctorDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class DoctorDTOMapper implements Function<Object[], DoctorDTO> {

    public DoctorDTO apply(Object[] obj) {

            Doctor doctor = (Doctor) obj[0];
            User user = (User) obj[1];
            Category category = (Category) obj[2];

        return new DoctorDTO(
                doctor.getId(),
                doctor.getRate(),
                doctor.getAbout(),
                doctor.getEducation(),
                doctor.getExperience(),
                doctor.getPricePerMin(),
                doctor.getWorkplace(),
                doctor.isOnline(),
                user.getId(),
                user.getFullName(),
                category.getId(),
                category.getName(),
                category.getSlag()
        );
    }

}
