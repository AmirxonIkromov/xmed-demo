package com.example.xmed.service;

import com.example.xmed.config.JwtService;
import com.example.xmed.entity.OTP;
import com.example.xmed.entity.UserAgent;
import com.example.xmed.payload.AuthenticationResponse;
import com.example.xmed.payload.SmsRequestDTO;
import com.example.xmed.repository.OTPRepository;
import com.example.xmed.repository.UserAgentRepository;
import com.example.xmed.repository.UserRepository;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.text.DecimalFormat;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final VonageClient vonageClient;
    private final UserRepository userRepository;
    private final OTPRepository otpRepository;
    private final JwtService jwtService;
    private final UserAgentRepository userAgentRepository;


    public ResponseEntity<?> sendSms(String phoneNumber) {
        if (isPhoneNumberValid(phoneNumber)) {
            String to = phoneNumber;
            String from = "+998909259909";
            String otpCode = generateOTP();
            String message = "verification code is " + otpCode;

            SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(
                    new TextMessage(from, to, message));

            otpRepository.save(new OTP(to, otpCode));

            if (response.getMessages().get(0).getStatus() != MessageStatus.OK) {
                throw new RuntimeException("Failed to send SMS");

            }
        } else {
            throw new IllegalArgumentException(
                    "Phone number [" + phoneNumber + "] is not a valid number");
        }
        return ResponseEntity.ok("SMS sent");
    }

    public ResponseEntity<?> validateOTP(SmsRequestDTO smsRequestDTO) {
        var otpKeeper = otpRepository.findByPhoneNumber(smsRequestDTO.phoneNumber()).orElseThrow();
        if (smsRequestDTO.otp().equals(otpKeeper.getOtpCode())) {
            otpRepository.delete(otpKeeper);
//            response.sendRedirect("http://localhost:8080/user-config/reset-password");
            var user = userRepository.findByPhoneNumber(smsRequestDTO.phoneNumber()).orElseThrow();
            var userAgent = AuthService.userAgentParser(user, smsRequestDTO.userAgentDTO());
            userAgentRepository.save(userAgent);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user, null,
                    AuthorityUtils.createAuthorityList("USER")
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            var jwtToken = jwtService.generateToken(user);
            return ResponseEntity.status(HttpStatus.OK).body(AuthenticationResponse.builder().token(jwtToken).build());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP please retry");
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        var user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();
        return user.getPhoneNumber().equals(phoneNumber);
    }

    private String generateOTP() {
        return new DecimalFormat("0000").format(new Random().nextInt(9999));
    }


}
