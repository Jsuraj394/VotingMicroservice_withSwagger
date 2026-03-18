package com.Sura.CandidateService.Repository;

import com.Sura.CandidateService.Entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepo extends JpaRepository<Candidate,Long> {
    List<Candidate> findAllByOrderByVoteCountDesc();

    boolean existsByAdharCardNo(Long adharCardNo);

}
