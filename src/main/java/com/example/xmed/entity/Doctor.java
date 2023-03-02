package com.example.xmed.entity;

import com.example.xmed.enums.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float rate;
    private String about;
    private String education;
    private Integer experience;
    private Integer pricePerMin;
    private String workplace;
    private boolean online;
    @OneToOne
    private Category category;
    @Enumerated(EnumType.STRING)
    private List<Language> receptionLanguage;
    @ManyToOne
    private User user;
}
