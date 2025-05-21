package com.hiringapp.utils.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfoDTO {

    private long id;

    @NotBlank(message = "DOB is required")
    private String dob;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(?i)(male|female|other|prefer not to say)$", message = "Gender must be male, female, other, or prefer not to say")
    private String gender;

    @NotBlank(message = "Address is required")
    private String address;

    private Long candidateId;
}
