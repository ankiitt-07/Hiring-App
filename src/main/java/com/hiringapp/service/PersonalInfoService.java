package com.hiringapp.service;

import com.hiringapp.exceptions.ResourceNotFoundException;
import com.hiringapp.model.entity.Candidate;
import com.hiringapp.model.entity.PersonalInfo;
import com.hiringapp.repository.CandidateRepository;
import com.hiringapp.repository.PersonalInfoRepository;
import com.hiringapp.utils.dtos.PersonalInfoDTO;
import com.hiringapp.utils.mapper.PersonalInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonalInfoService {

    @Autowired
    private PersonalInfoRepository personalInfoRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PersonalInfoMapper personalInfoMapper;

    public PersonalInfoDTO savePersonalInfo(PersonalInfoDTO dto) {
        Candidate candidate = candidateRepository.findById(dto.getCandidateId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        PersonalInfo entity = personalInfoMapper.toEntity(dto);
        entity.setCandidate(candidate);

        PersonalInfo saved = personalInfoRepository.save(entity);
        return personalInfoMapper.toDto(saved);
    }

    public Optional<PersonalInfoDTO> getPersonalInfoById(Long id) {
        return personalInfoRepository.findById(id)
                .map(personalInfoMapper::toDto);
    }
}
