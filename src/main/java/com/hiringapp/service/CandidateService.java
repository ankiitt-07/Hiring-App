package com.hiringapp.service;

import com.hiringapp.model.entity.Candidate;
import com.hiringapp.model.enums.CandidateStatus;
import com.hiringapp.exceptions.CandidateNotFoundException;
import com.hiringapp.exceptions.InvalidStatusTransitionException;
import com.hiringapp.exceptions.ResourceNotFoundException;
import com.hiringapp.repository.CandidateRepository;
import com.hiringapp.model.mapper.CandidateMapper;
import com.hiringapp.service.producer.DocumentProducer;
import com.hiringapp.service.producer.RabbitProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private DocumentProducer documentProducer;

    @Autowired
    private RabbitProducer rabbitProducer;

    @Autowired
    private CandidateMapper candidateMapper;

    public List<Candidate> getAllCandidates() {
        List<Candidate> candidates = candidateRepository.findAllCandidates();

        for (Candidate candidate : candidates) {
            if (candidate.getStatus() != null) {
                switch (candidate.getStatus()) {
                    case OFFERED:
                    case REJECTED:
                        log.info("Sending status mail for Candidate ID: {}", candidate.getId());
                        rabbitProducer.sendCandidate(candidateMapper.toDto(candidate));
                        break;
                    default:
                        log.info("Skipping Candidate ID: {} with status: {}", candidate.getId(), candidate.getStatus());
                }
            }
        }

        return candidates;
    }

    @Cacheable(value = "candidates", key = "#id")
    public Candidate getCandidatesById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new CandidateNotFoundException(id));
    }

    @CachePut(value = "candidates", key = "#result.id")
    public Candidate addCandidate(Candidate candidate) throws Exception {
        if (candidateRepository.existsById(candidate.getId())) {
            throw new Exception("Candidate already exists");
        }

        candidate.setStatus(CandidateStatus.APPLIED);
        Candidate saved = candidateRepository.save(candidate);

        log.info("Candidate saved with ID: {}", saved.getId());
        documentProducer.sendCandidateId(saved.getId());

        return saved;
    }

    @CachePut(value = "candidates", key = "#id")
    public Candidate updateCandidate(Long id, Candidate candidate) {
        Optional<Candidate> existingCandidateOptional = candidateRepository.findById(id);
        if (existingCandidateOptional.isPresent()) {
            Candidate existingCandidate = existingCandidateOptional.get();

            existingCandidate.setFullName(candidate.getFullName());
            existingCandidate.setEmail(candidate.getEmail());
            existingCandidate.setPhoneNumber(candidate.getPhoneNumber());

            CandidateStatus currentStatus = existingCandidate.getStatus();
            CandidateStatus newStatus = candidate.getStatus();

            if (newStatus != null && currentStatus != newStatus) {
                if (!currentStatus.canTransitionTo(newStatus)) {
                    throw new InvalidStatusTransitionException("Invalid status transition from " + currentStatus + " to " + newStatus);
                }
                existingCandidate.setStatus(newStatus);
            }

            return candidateRepository.save(existingCandidate);
        }
        throw new ResourceNotFoundException("Candidate not found with ID: " + id);
    }

    @CacheEvict(value = "candidates", key = "#id")
    public void deleteCandidate(Long id) throws Exception {
        if (!candidateRepository.existsById(id)) {
            throw new Exception("Candidate not found");
        }
        candidateRepository.deleteById(id);
    }

    public long countCandidates() {
        return candidateRepository.count();
    }

    public ResponseEntity<Resource> generateCandidateReport() {
        List<Candidate> candidates = candidateRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Candidates");
            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Full Name");
            header.createCell(2).setCellValue("Email");
            header.createCell(3).setCellValue("Phone");
            header.createCell(4).setCellValue("Status");

            int rowNum = 1;
            for (Candidate candidate : candidates) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(candidate.getId());
                row.createCell(1).setCellValue(candidate.getFullName());
                row.createCell(2).setCellValue(candidate.getEmail());
                row.createCell(3).setCellValue(candidate.getPhoneNumber());
                row.createCell(4).setCellValue(candidate.getStatus() != null ? candidate.getStatus().toString() : "N/A");
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=candidate_report.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(resource.contentLength())
                    .body(resource);

        } catch (Exception e) {
            log.error("Error generating report: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
