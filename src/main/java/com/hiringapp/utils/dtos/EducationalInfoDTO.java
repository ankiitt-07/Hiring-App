package com.hiringapp.utils.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationalInfoDTO {
    private Long id;

    @NotBlank(message = "Degree name cannot be empty")
    private String degreeName;

    @NotBlank(message = "Institution name cannot be empty")
    private String institutionName;

    @NotBlank(message = "Year of passing cannot be empty")
    @Pattern(regexp = "^(19[5-9][0-9]|20[0-9][0-9])$", message = "Year must be a valid year")
    private int yearOfPassing;

    private Long candidateId;
}
