package com.hiringapp.controller;


import com.hiringapp.model.entity.Candidate;
import com.hiringapp.service.CandidateService;
import com.hiringapp.service.RabbitProducer;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@Slf4j
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

//    @Autowired
//    private RabbitProducer rabbitProducer;

    @GetMapping("/all")
    public List<Candidate> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @GetMapping("/{id}")
    public Candidate getCandidateById(@PathVariable Long id) {
        return candidateService.getCandidatesById(id);
    }

    @PostMapping("/add")
    public Candidate createCandidate(@Valid @RequestBody Candidate candidate) throws Exception {
//         rabbitProducer.sendCandidate(candidateMapper.toDto(candidate));
        log.info("Candidate created: " + candidate);
        return candidateService.addCandidate( candidate );
    }

    @PutMapping("/{id}")
    public Candidate updateCandidate(@Valid @PathVariable Long id,@RequestBody Candidate candidate) {
        return candidateService.updateCandidate( id, candidate );
    }

    @DeleteMapping("/{id}")
    public void deleteCandidate(@PathVariable Long id) throws Exception {
        candidateService.deleteCandidate(id);
    }

    @GetMapping("/count")
    public long getCandidateCount() {
        return candidateService.countCandidates();
    }

    @GetMapping("/report")
    public ResponseEntity<Resource> downloadCandidateReport() {
        return candidateService.generateCandidateReport();
    }
}
