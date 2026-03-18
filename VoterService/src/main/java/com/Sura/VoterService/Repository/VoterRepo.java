package com.Sura.VoterService.Repository;


import com.Sura.VoterService.Entity.Voter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoterRepo extends JpaRepository<Voter, Long> {
    boolean existsByVoterEmail(String email);  //  used in old code

    List<Voter> findByCandidateId(Long candidateId);

    boolean existsByCandidateId(Long candidateId);

    boolean existsByAdharCardNo(Long adharCardNo);

//    Voter findByVoterName(String voterName);
//    Voter findByVoterNameIgnoreCase(String voterName);
}
