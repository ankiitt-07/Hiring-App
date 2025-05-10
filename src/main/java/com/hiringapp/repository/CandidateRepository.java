package com.hiringapp.repository;

import com.hiringapp.model.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    @Query("SELECT DISTINCT c FROM Candidate c ORDER BY c.fullName ASC ")
    List<Candidate> findAllCandidates();
}
