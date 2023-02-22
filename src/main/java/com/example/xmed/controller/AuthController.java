package com.example.xmed.controller;

import com.example.xmed.entity.UserAgent;
import com.example.xmed.payload.*;
import com.example.xmed.service.AuthService;
import com.example.xmed.service.SmsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SmsService smsService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserDTO registerUserDTO) {
        return ResponseEntity.ok(authService.register(registerUserDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDTO loginUserDTO) {
        return ResponseEntity.ok(authService.login(loginUserDTO));
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

}