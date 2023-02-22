package com.example.xmed.payload;

import jakarta.validation.constraints.NotBlank;

public record SmsRequestDTO(@NotBlank String phoneNumber,
                            @NotBlank String otp, UserAgentDTO userAgentDTO) {

}
