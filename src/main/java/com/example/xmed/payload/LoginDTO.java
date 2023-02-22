package com.example.xmed.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    private String phoneNumber;
    private String password;


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
    private LocalDateTime tokenDate;
    private boolean deletedStatus = false;
    private LocalDateTime deletedDate;
}
