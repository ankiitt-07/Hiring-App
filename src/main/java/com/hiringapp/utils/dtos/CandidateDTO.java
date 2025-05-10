package com.hiringapp.utils.dtos;

import com.hiringapp.model.enums.CandidateStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateDTO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Full name is required")
    @Pattern(regexp = "^[A-Z][a-z]+\\s[A-Z][a-z]+$", message = "Full Name start with capital letter and space between first name and last name")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "phone number must be exactly 10 digits")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Candidate status cannot be empty")
    private CandidateStatus status;

    private LocalDateTime sentAt;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}