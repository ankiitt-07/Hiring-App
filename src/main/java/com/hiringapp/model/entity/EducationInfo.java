package com.hiringapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 50)
    private String degreeName;

    @Column(nullable = false, length = 50)
    private String institutionName;

    @Column(nullable = false, length = 50)
    private int yearOfPassing;
}
