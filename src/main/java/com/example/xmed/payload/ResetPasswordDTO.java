package com.example.xmed.payload;

import lombok.Data;

@Data
public class ResetPasswordDTO {

    private String password;
    private String confirmPassword;
    private String phoneNumber;
}
