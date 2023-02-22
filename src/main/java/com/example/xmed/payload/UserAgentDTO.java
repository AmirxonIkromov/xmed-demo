package com.example.xmed.payload;

import lombok.Data;

@Data

public class UserAgentDTO {

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
