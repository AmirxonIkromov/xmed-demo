package com.example.xmed.repository;

import com.example.xmed.entity.Category;
import com.example.xmed.entity.Doctor;
import com.example.xmed.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.print.Doc;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("select d, u from Doctor d join d.user u " +
            "where (u.fullName ilike concat('%',:text,'%'))" +
            "and d.pricePerMin >= :filterByPriceMin " +
            "and d.pricePerMin <= :filterByPriceMax " +
            "and d.experience >= :filterByExperience " +
            "and d.rate >= :filterByRate " +
            "and((:filterByOnline = true and d.online = true) " +
            "or (:filterByOffline = true and d.online = false)) " +
            "order by " +
            "case when :sortBy = 'pricePerMin' then d.pricePerMin end asc, " +
            "case when :sortBy = 'experience' then d.experience end desc, " +
            "case when :sortBy = 'rate' then d.rate end desc")
    List<Object[]> findDoctors(String text, Integer filterByPriceMin,
                             Integer filterByPriceMax, Integer filterByExperience,
                             Float filterByRate, String sortBy, Boolean filterByOffline, Boolean filterByOnline);

    @Query("select d, u, c from Doctor d join d.user u join d.categories c " +
            "where c.id = :categoryId " +
            "and d.pricePerMin >= :filterByPriceMin " +
            "and d.pricePerMin <= :filterByPriceMax " +
            "and d.experience >= :filterByExperience " +
            "and d.rate >= :filterByRate " +
            "and((:filterByOnline = true and d.online = true) " +
            "or (:filterByOffline = true and d.online = false)) " +
            "order by " +
            "case when :sortBy = 'pricePerMin' then d.pricePerMin end asc, " +
            "case when :sortBy = 'experience' then d.experience end desc, " +
            "case when :sortBy = 'rate' then d.rate end desc")
    List<Object[]> findDoctorsByCategory(Long categoryId, Integer filterByPriceMin,
                          Integer filterByPriceMax, Integer filterByExperience,
                          Float filterByRate, String sortBy, Boolean filterByOffline, Boolean filterByOnline );

    @Query("SELECT COUNT(d), c FROM Doctor d JOIN d.categories c GROUP BY c.id ORDER BY c.name\n")
    List<Object[]> searchDoctorByCategory();

}
