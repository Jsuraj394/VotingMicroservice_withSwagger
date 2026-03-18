package com.Sura.VoterService.Service;

import com.Sura.VoterService.DTO.VoterResponseDTO;
import com.Sura.VoterService.DTO.VoterResponseForCandidateDTO;
import com.Sura.VoterService.Entity.Voter;
import com.Sura.VoterService.Exception.DuplicateResourceException;
import com.Sura.VoterService.Exception.ResourceNotFoundException;
import com.Sura.VoterService.Repository.VoterRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VoterService {
    private final VoterRepo voterRepo;
    private final CandidateClient candidateClient;

    @Autowired
    public VoterService(VoterRepo voterRepo, CandidateClient candidateClient) {
        this.voterRepo = voterRepo;
        this.candidateClient = candidateClient;
    }

    public Voter registerVoter(Voter voter) {
        if ( voterRepo.existsByAdharCardNo(voter.getAdharCardNo())) {
            throw new DuplicateResourceException("Voter with Adhar number " + voter.getAdharCardNo()  + " already exists.");
        }
        return voterRepo.save(voter);
    }

    public List<Voter> getAllVoters() {
        return voterRepo.findAll();
    }

    public Voter getVoterById(Long id) {
        return voterRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voter with ID " + id + " not found."));
    }

    public List<VoterResponseForCandidateDTO> getVoteByCandidateID(Long id){

        List<Voter> voters = voterRepo.findByCandidateId(id);

        System.out.println("this is voter "+voters);
        List<VoterResponseForCandidateDTO> voterResponseForCandidateDTOlist = new ArrayList<>();
        for (Voter voter : voters){
            voterResponseForCandidateDTOlist.add(new VoterResponseForCandidateDTO(voter.getVoterName(),voter.getHasVoted()==true ? "Yes":"No"));
        }
        return voterResponseForCandidateDTOlist;
    }

    @Transactional
    public String deleteVoter(Long id, String authorization) {
        Voter voter = voterRepo.findById(id).orElse(null);
        if (!voterRepo.existsById(id)) {
            throw new ResourceNotFoundException("Voter with ID " + id + " not found.");
        }
        if(voter.getCandidateId() !=null) {
            boolean flag = candidateClient.removeVoteFromCandidate(voter.getCandidateId(), authorization);
            System.out.println("Vote removed from candidate with ID " + voter.getCandidateId());
        }else{
            System.out.println("Voter with ID " + id + " has not voted for any candidate.");
        }

        voterRepo.deleteById(id);
        return "Voter with ID " + id + " has been deleted.";
    }

    public Voter updateVoter(Voter voter, Long id) {
        if (!voterRepo.existsById(voter.getVoterId())) {
            throw new ResourceNotFoundException("Voter with ID " + id + " not found.");
        }

        Voter existingVoter = voterRepo.findById(id).orElse(null);
        if(voter.getVoterName() != null) {
            existingVoter.setVoterName(voter.getVoterName());
        }
        if(voter.getVoterEmail() != null) {
            existingVoter.setVoterEmail(voter.getVoterEmail());
        }
        if(voter.getHasVoted() != null) {
            existingVoter.setHasVoted(voter.getHasVoted());
        }
        if(voter.getAdharCardNo()!=null){
            existingVoter.setAdharCardNo(voter.getAdharCardNo());
        }

        voterRepo.save(existingVoter);
        return existingVoter;
    }

    public void detachVoterFromCandidate(Long candidateId) {
        if (!voterRepo.existsByCandidateId(candidateId)) {
            System.out.println("Voter with candidateId " + candidateId + " not found.");
            return;
        }

        List<Voter> voters = voterRepo.findByCandidateId(candidateId);
        for (Voter voter : voters) {
            voter.setHasVoted(false);
            voter.setCandidateId(null);
            voterRepo.save(voter);
        }
    }

    @Transactional
    public String castVote(Long voterId, Long candidateId, String authorization) {
        Voter voter = voterRepo.findById(voterId).orElse(null);
        if (voter == null) {
            throw new ResourceNotFoundException("Voter with ID " + voterId + " not found.");
        }
        if (Boolean.TRUE.equals(voter.getHasVoted())) {
            throw new RuntimeException("Voter with ID " + voterId + " has already voted.");
        }
        voter.setHasVoted(true);
        voter.setCandidateId(candidateId);

        boolean ok = candidateClient.addVoteToCandidate(candidateId, authorization);
        if(!ok) {
            throw new RuntimeException("Candidate with ID " + candidateId + " not found in Candidate Service.");
        }

        voterRepo.save(voter);
        return "Voter with ID " + voterId + " has voted for candidate with ID " + candidateId + ".";
    }
}