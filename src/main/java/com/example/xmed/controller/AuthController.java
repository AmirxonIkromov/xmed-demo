package com.example.xmed.controller;

import com.example.xmed.entity.UserAgent;
import com.example.xmed.payload.*;
import com.example.xmed.repository.UserRepository;
import com.example.xmed.service.AuthService;
import com.example.xmed.service.SmsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SmsService smsService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(authService.register(registerDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody SmsRequestDTO smsRequestDTO) {
        return ResponseEntity.ok(smsService.sendSms(smsRequestDTO.phoneNumber()));
    }

    @PostMapping("/checkOTP")
    public ResponseEntity<?> checkOTP(@RequestBody SmsRequestDTO smsRequestDTO) {
        return ResponseEntity.ok(smsService.validateOTP(smsRequestDTO));
    }

    @PostMapping("/pinCode-login")
    public ResponseEntity<AuthenticationResponse> pinCodeLogin(@RequestBody PinCodeLoginDTO pinCodeLoginDTO) {
        return ResponseEntity.ok(authService.pinCodeLogin(pinCodeLoginDTO));
    }

//    @GetMapping
//    public ResponseEntity<?> time(){
//        return ResponseEntity.ok(LocalDateTime.now().format());
//    }

}