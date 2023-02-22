package com.example.xmed.service;

import com.example.xmed.config.JwtService;
import com.example.xmed.entity.User;
import com.example.xmed.entity.UserAgent;
import com.example.xmed.payload.AuthenticationResponse;
import com.example.xmed.payload.LoginDTO;
import com.example.xmed.payload.PinCodeLoginDTO;
import com.example.xmed.payload.RegisterDTO;
import com.example.xmed.repository.UserAgentRepository;
import com.example.xmed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserAgentRepository userAgentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SmsService smsService;


    public ResponseEntity<?> register(@NotNull RegisterDTO registerDTO) {
        var user = User.builder()
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .phoneNumber(registerDTO.getPhoneNumber())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .enabled(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .accountNonExpired(true)
                .language(registerDTO.getLanguage())
                .role(registerDTO.getRole())
                .build();
        userRepository.save(user);

        var userAgent = UserAgent.builder()
                .userAgent(registerDTO.getUserAgent())
                .ip(registerDTO.getIp())
                .referer(registerDTO.getReferer())
                .fullURL(registerDTO.getFullURL())
                .clientOS(registerDTO.getClientOS())
                .browser(registerDTO.getBrowser())
                .appVersion(registerDTO.getAppVersion())
                .hardwareInfo(registerDTO.getHardwareInfo())
                .platform(registerDTO.getPlatform())
                .city(registerDTO.getCity())
                .country(registerDTO.getCountry())
                .region(registerDTO.getRegion())
                .location(registerDTO.getLocation())
                .org(registerDTO.getOrg())
                .timezone(registerDTO.getTimezone())
                .readMe(registerDTO.getReadMe())
                .user(user).build();
        userAgentRepository.save(userAgent);
        smsService.sendSms(user.getPhoneNumber());
        return ResponseEntity.ok().build();

    }

    public ResponseEntity<?> login(@NotNull LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getPhoneNumber(), loginDTO.getPassword()
                )
        );
        var user = userRepository.findByPhoneNumber(loginDTO.getPhoneNumber()).orElseThrow();
        var userAgentRep = userAgentRepository.findByUser_IdAndDeletedStatusFalse(user.getId()).orElseThrow();

        var userAgent = UserAgent.builder()
                .userAgent(loginDTO.getUserAgent())
                .ip(loginDTO.getIp())
                .referer(loginDTO.getReferer())
                .fullURL(loginDTO.getFullURL())
                .clientOS(loginDTO.getClientOS())
                .browser(loginDTO.getBrowser())
                .appVersion(loginDTO.getAppVersion())
                .hardwareInfo(loginDTO.getHardwareInfo())
                .platform(loginDTO.getPlatform())
                .city(loginDTO.getCity())
                .country(loginDTO.getCountry())
                .region(loginDTO.getRegion())
                .location(loginDTO.getLocation())
                .org(loginDTO.getOrg())
                .timezone(loginDTO.getTimezone())
                .readMe(loginDTO.getReadMe())
                .user(user).build();
        userAgentRep.setDeletedStatus(true);
        userAgentRepository.save(userAgentRep);
        userAgentRepository.save(userAgent);

        if (userAgentRep.getPlatform().equals(loginDTO.getPlatform())) {
            var jwtToken = jwtService.generateToken(user);
            return ResponseEntity.ok(AuthenticationResponse.builder().token(jwtToken).build());
        }
        smsService.sendSms(user.getPhoneNumber());
        return ResponseEntity.ok().build();
    }

    public AuthenticationResponse pinCodeLogin(PinCodeLoginDTO pinCodeLoginDTO) {
        var user = userRepository.findByPhoneNumber(pinCodeLoginDTO.getPhoneNumber()).orElseThrow();

        if (user.getPinCode().equals(pinCodeLoginDTO.getPinCode())) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user, null,
                    AuthorityUtils.createAuthorityList("USER")
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).build();
        }
        return AuthenticationResponse.builder().token("pin code invalid").build();
    }

}
