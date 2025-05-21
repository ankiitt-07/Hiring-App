package com.hiringapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationalInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String degree;
    private String university;
    private Integer passingYear;
    private Double percentage;

    @OneToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
}
