package com.example.xmed.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    @Column(columnDefinition = "boolean default false")
    private boolean isVerified = false;

    private LocalDateTime tokenDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(columnDefinition = "boolean default false")
    private boolean deletedStatus = false;

    private LocalDateTime deletedDate;

}
