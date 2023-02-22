package com.example.xmed.service;

import com.example.xmed.entity.User;
import com.example.xmed.payload.ChangePasswordDTO;
import com.example.xmed.payload.ResetPasswordDTO;
import com.example.xmed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<?> changePassword(@NotNull ChangePasswordDTO changePasswordDTO,
                                                     User currentUser) {

        var user = userRepository.findById(currentUser.getId()).orElseThrow();
        var matches = passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword());
        if (!matches)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect current password");

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The two given passwords do not match");

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Password changed");
    }

    public ResponseEntity<?> resetPassword(ResetPasswordDTO resetPasswordDTO) {
        if(resetPasswordDTO.getPassword().equals(resetPasswordDTO.getConfirmPassword())) {
            var user = userRepository.findByPhoneNumber(resetPasswordDTO.getPhoneNumber()).orElseThrow();
            user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok("Password set");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("given to password not matches");
    }

    public ResponseEntity<?> setPinCode(String pinCode, User currentUser) {
        var user = userRepository.findById(currentUser.getId()).orElseThrow();
        user.setPinCode(pinCode);
        userRepository.save(user);
        return ResponseEntity.ok("pin code set");
    }

}
