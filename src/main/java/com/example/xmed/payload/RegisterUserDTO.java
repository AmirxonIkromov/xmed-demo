package com.example.xmed.payload;

import lombok.Data;

@Data
public class RegisterUserDTO {

    private RegisterDTO registerDTO;
    private UserAgentDTO userAgentDTO;
}
