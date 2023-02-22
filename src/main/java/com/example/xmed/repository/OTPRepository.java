package com.example.xmed.repository;

import com.example.xmed.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP, Long> {

    Optional<OTP> findByPhoneNumber(String phoneNumber);
}
