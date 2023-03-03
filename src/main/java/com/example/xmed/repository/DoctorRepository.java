package com.example.xmed.repository;

import com.example.xmed.entity.Category;
import com.example.xmed.entity.Doctor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

//    @Query(value = "select * from doctor join users on doctor.user_id = users.id " +
//                    "where users.full_name like % :text %", nativeQuery = true)
//    List<Doctor> findDoctorsByFullNameContaining(String text);

    @Query("select d, u, c from Doctor d join d.user u join d.category c " +
            "where lower(u.fullName) like lower(concat('%',:text, '%')) " +
            "or lower(c.name) like lower(concat('%',:text, '%'))")
    List<Doctor> findDoctorsByContaining(@Param("text") String text);

    List<Doctor> findAllByCategoryId(Long categoryId);

    List<Doctor> findByCategoryId(Long category_id, Sort sort);
}
