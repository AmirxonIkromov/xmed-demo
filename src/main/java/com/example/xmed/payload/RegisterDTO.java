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


//    UserAgent's fields
    private String userAgent;
    private String ip;
    private String referer;
    private String fullURL;
    private String clientOS;
    private String browser;
    private String appVersion;
    private String hardwareInfo;
    private String platform;
    private String city;
    private String country;
    private String region;
    private String location;
    private String org;
    private String timezone;
    private String readMe;
    private boolean isVerified = false;
    private String tokenDate;
    private boolean deletedStatus = false;
    private String  deletedDate;
}
