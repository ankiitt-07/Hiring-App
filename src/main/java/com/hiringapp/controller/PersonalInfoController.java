package com.hiringapp.controller;

import com.hiringapp.service.PersonalInfoService;
import com.hiringapp.utils.dtos.PersonalInfoDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidates/personal-info")
public class PersonalInfoController {

    private final PersonalInfoService personalInfoService;

    public PersonalInfoController(PersonalInfoService personalInfoService) {
        this.personalInfoService = personalInfoService;
    }

    @PostMapping
    public ResponseEntity<PersonalInfoDTO> create(@Valid @RequestBody PersonalInfoDTO dto) {
        PersonalInfoDTO saved = personalInfoService.savePersonalInfo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalInfoDTO> getById(@PathVariable Long id) {
        return personalInfoService.getPersonalInfoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
