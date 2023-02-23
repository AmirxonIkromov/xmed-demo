package com.example.xmed.controller;


import com.example.xmed.aop.CurrentUser;
import com.example.xmed.entity.User;
import com.example.xmed.payload.ChangePasswordDTO;
import com.example.xmed.payload.ResetPasswordDTO;
import com.example.xmed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user-config")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO,
                                            @CurrentUser User user) {
        return ResponseEntity.ok(userService.changePassword(changePasswordDTO, user));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        return ResponseEntity.ok(userService.resetPassword(resetPasswordDTO));
    }

    @PostMapping("/set-pinCode")
    public ResponseEntity<?> setPinCode(@RequestParam String pinCode, @CurrentUser User currentUser) {
        return ResponseEntity.ok(userService.setPinCode(pinCode, currentUser));
    }

}
