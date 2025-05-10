package com.hiringapp.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import com.hiringapp.utils.dtos.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bank_info")
public class BankInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String bankName;

    @Column(nullable = false, length = 50)
    private String ifscCode;

    @Column(nullable = false, length = 50)
    private String accountNumber;

//
//    @JsonBackReference
//    @OneToOne(mappedBy = "bankInfo") // foreign key
//    private Candidate candidate;

}
