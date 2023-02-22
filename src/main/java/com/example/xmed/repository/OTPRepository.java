package com.example.xmed.repository;

import com.example.xmed.entity.OTPKeeper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTPKeeper, Long> {

    Optional<OTPKeeper> findByPhoneNumber(String phoneNumber);
}
