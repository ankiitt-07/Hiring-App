package com.hiringapp.utils.mapper;

import com.hiringapp.model.entity.PersonalInfo;
import com.hiringapp.utils.dtos.PersonalInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface PersonalInfoMapper {

    @Mapping(target = "dob", expression = "java(parseDate(dto.getDob()))")
    @Mapping(source = "candidateId", target = "candidate.id")
    PersonalInfo toEntity(PersonalInfoDTO dto);

    @Mapping(target = "dob", expression = "java(formatDate(entity.getDob()))")
    @Mapping(source = "candidate.id", target = "candidateId")
    PersonalInfoDTO toDto(PersonalInfo entity);

    default LocalDate parseDate(String dob) {
        return dob == null ? null : LocalDate.parse(dob);
    }

    default String formatDate(LocalDate dob) {
        return dob == null ? null : dob.toString();
    }
}
