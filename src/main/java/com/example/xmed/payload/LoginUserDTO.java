package com.example.xmed.payload;

import lombok.Data;

@Data
public class LoginUserDTO {

    private LoginDTO loginDTO;
    private UserAgentDTO userAgentDTO;
}
