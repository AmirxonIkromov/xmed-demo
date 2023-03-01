package com.example.xmed.entity;

import com.example.xmed.enums.Language;
import com.example.xmed.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;
import lombok.*;


import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    @Column(nullable = false)
    private String password;
    private Float rate;
    private String about;
    private String education;
    private Integer experience;
    private Integer pricePerMin;
    private String workplace;
    private boolean online;
    @OneToMany
    private List<Category> category;
    @Enumerated(EnumType.STRING)
    private List<Language> receptionLanguage;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    private String pinCode;
    private boolean enabled = true;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    @Enumerated(EnumType.STRING)
    private Language language;
    @Enumerated(EnumType.STRING)
    private Role role;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getUsername()));
    }

    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

}
