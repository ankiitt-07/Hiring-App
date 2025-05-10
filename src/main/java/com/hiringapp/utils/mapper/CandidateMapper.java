package com.hiringapp.utils.mapper;

import com.hiringapp.model.entity.Candidate;
import com.hiringapp.model.enums.CandidateStatus;
import com.hiringapp.utils.dtos.CandidateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

@EnableJpaAuditing
@Mapper(componentModel = "spring")
public interface CandidateMapper {

//    @Mapping(target = "status", expression = "java(CandidateStatus.valueOf(dto.getStatus()))")
    Candidate toEntity(CandidateDTO dto);

//    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatusToString")
    CandidateDTO toDto(Candidate candidate);

    @Named("mapStatusToEnum")
    default CandidateStatus mapStatusToEnum(String status) {
        return status == null ? null : CandidateStatus.valueOf(status);
    }

    @Named("mapStatusToString")
    default String mapStatusToString(CandidateStatus status) {
        return status == null ? null : status.name();
    }
}

//@Component
//public class CandidateMapper{
//    public Candidate toEntity(CandidateDTO dto) {
//        Candidate candidate = new Candidate();
//        candidate.setId(dto.getId());
//        candidate.setFullName(dto.getFullName());
//        candidate.setEmail(dto.getEmail());
//        candidate.setPhoneNumber(dto.getPhoneNumber());
//        candidate.setStatus(dto.getStatus().name()); // if entity uses String
//        return candidate;
//    }
//
//    public CandidateDTO toDto(Candidate candidate) {
//        CandidateDTO dto = new CandidateDTO();
//        dto.setId(candidate.getId());
//        dto.setFullName(candidate.getFullName());
//        dto.setEmail(candidate.getEmail());
//        dto.setPhoneNumber(candidate.getPhoneNumber());
//        dto.setStatus(CandidateStatus.valueOf(candidate.getStatus())); // if entity uses String
//        return dto;
//    }
//
//}