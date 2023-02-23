package com.example.xmed.service;

import com.example.xmed.config.JwtService;
import com.example.xmed.entity.User;
import com.example.xmed.entity.UserAgent;
import com.example.xmed.payload.*;
import com.example.xmed.repository.UserAgentRepository;
import com.example.xmed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserAgentRepository userAgentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SmsService smsService;


    public ResponseEntity<?> register(@NotNull RegisterUserDTO registerUserDTO) {
        var user = User.builder()
                .firstName(registerUserDTO.getRegisterDTO().getFirstName())
                .lastName(registerUserDTO.getRegisterDTO().getLastName())
                .phoneNumber(registerUserDTO.getRegisterDTO().getPhoneNumber())
                .password(passwordEncoder.encode(registerUserDTO.getRegisterDTO().getPassword()))
                .enabled(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .accountNonExpired(true)
                .language(registerUserDTO.getRegisterDTO().getLanguage())
                .role(registerUserDTO.getRegisterDTO().getRole())
                .build();
        userRepository.save(user);
        var userAgent = userAgentParser(user, registerUserDTO.getUserAgentDTO());
        userAgentRepository.save(userAgent);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerUserDTO.getRegisterDTO().getPhoneNumber(),
                        registerUserDTO.getRegisterDTO().getPassword()
                )
        );
        var jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(AuthenticationResponse.builder().token(jwtToken).build());

//        smsService.sendSms(user.getPhoneNumber());
//        return ResponseEntity.ok().build();

    }

    public ResponseEntity<?> login(@NotNull LoginUserDTO loginUserDTO) {
        try {
            var user = userRepository.findByPhoneNumber(loginUserDTO.getLoginDTO()
                    .getPhoneNumber()).orElseThrow();
            var userAgents = userAgentRepository.findAllByUser_Id(user.getId());

            for (UserAgent userAgent : userAgents) {
                if (userAgent.getPlatform().equals("Web"))
                    if (userAgent.getBrowser().equals(loginUserDTO.getUserAgentDTO().getBrowser())) {
                        authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                        loginUserDTO.getLoginDTO().getPhoneNumber(),
                                        loginUserDTO.getLoginDTO().getPassword()
                                )
                        );
                        var jwtToken = jwtService.generateToken(user);
                        return ResponseEntity.ok(AuthenticationResponse.builder().token(jwtToken).build());
                    }
                if (userAgent.getPlatform().equals("IOS") || userAgent.getPlatform().equals("Android"))
                    if (userAgent.getClientOS().equals(loginUserDTO.getUserAgentDTO().getClientOS())) {
                        authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                        loginUserDTO.getLoginDTO().getPhoneNumber(),
                                        loginUserDTO.getLoginDTO().getPassword()
                                )
                        );
                        var jwtToken = jwtService.generateToken(user);
                        return ResponseEntity.ok(AuthenticationResponse.builder().token(jwtToken).build());
                    }
            }
            smsService.sendSms(user.getPhoneNumber());
            return ResponseEntity.ok().build();

        } catch (NoSuchElementException ex) {
            ex.getStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no such user");
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

    static UserAgent userAgentParser(User user, UserAgentDTO userAgentDTO) {
        return UserAgent.builder()
                .userAgent(userAgentDTO.getUserAgent())
                .ip(userAgentDTO.getIp())
                .referer(userAgentDTO.getReferer())
                .fullURL(userAgentDTO.getFullURL())
                .clientOS(userAgentDTO.getClientOS())
                .browser(userAgentDTO.getBrowser())
                .appVersion(userAgentDTO.getAppVersion())
                .hardwareInfo(userAgentDTO.getHardwareInfo())
                .platform(userAgentDTO.getPlatform())
                .city(userAgentDTO.getCity())
                .country(userAgentDTO.getCountry())
                .region(userAgentDTO.getRegion())
                .location(userAgentDTO.getLocation())
                .org(userAgentDTO.getOrg())
                .timezone(userAgentDTO.getTimezone())
                .readMe(userAgentDTO.getReadMe())
                .user(user).build();
    }
}
