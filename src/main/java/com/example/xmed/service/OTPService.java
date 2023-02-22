package com.example.xmed.service;

import com.example.xmed.entity.User;
import com.example.xmed.payload.SmsRequestDTO;
import com.example.xmed.repository.UserRepository;
import com.example.xmed.twilio.TwilioConfig;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class OTPService {

    private final TwilioConfig twilioConfig;
    private final UserRepository userRepository;


    public void sendSms(SmsRequestDTO smsRequestDTO) {
//        if (isPhoneNumberValid(smsRequestDTO.phoneNumber())) {
        PhoneNumber to = new PhoneNumber(smsRequestDTO.phoneNumber());
        PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());
        String otpCode = generateOTP();
        String message = "verification code is " + otpCode;
        MessageCreator creator = Message.creator(to, from, message);
        creator.create();
//        } else {
//            throw new IllegalArgumentException(
//                    "Phone number [" + smsRequestDTO.phoneNumber() + "] is not a valid number");
//        }
    }

//    private boolean isPhoneNumberValid(String phoneNumber) {
//        var user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();
//        if (user.getId())
//            return true;
//    }

    private String generateOTP() {
        return new DecimalFormat("0000").format(new Random().nextInt(9999));
    }
}