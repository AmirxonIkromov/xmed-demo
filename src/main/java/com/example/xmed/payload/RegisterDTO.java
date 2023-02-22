package com.example.xmed.payload;

import com.example.xmed.enums.Language;
import com.example.xmed.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {

    private String firstName;
    private String lastName;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String password;
    private Role role;
    private Language language;

}
