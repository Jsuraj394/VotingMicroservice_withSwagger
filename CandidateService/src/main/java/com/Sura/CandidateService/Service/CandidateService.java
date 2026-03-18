package com.Sura.CandidateService.Service;


import com.Sura.CandidateService.DTO.DisplayVotingDTO;
import com.Sura.CandidateService.Entity.Candidate;
import com.Sura.CandidateService.Exception.DuplicateResourceException;
import com.Sura.CandidateService.Exception.ResourceNotFoundException;
import com.Sura.CandidateService.Repository.CandidateRepo;
import com.sura.generated.model.CandidateRequestDTO;
import com.sura.generated.model.VoterClientResposeDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

@Service
public class CandidateService {
    private final CandidateRepo candidateRepo;
    private final VoterClient voterClient;

    @Autowired
    public CandidateService(CandidateRepo candidateRepo, VoterClient voterClient) {
        this.candidateRepo = candidateRepo;
        this.voterClient = voterClient;
    }

    public Candidate registerCandidate(Candidate candidate) {

        if ( candidateRepo.existsByAdharCardNo(candidate.getAdharCardNo())) {
            throw new DuplicateResourceException("candidate with Adhar number " + candidate.getAdharCardNo()  + " already exists.");
        }

        System.out.println("Registering candidate: " + candidate.getCandidateName() + ", Party: " + candidate.getParty());
        return candidateRepo.save(candidate);
    }

    public Candidate getCandidateById(Long id) {
        Candidate candidate = candidateRepo.findById(id).orElse(null);
        if(candidate==null){
            throw new ResourceNotFoundException("Candidate with ID " + id + " not found.");
        }
        return candidate;
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepo.findAll();
    }

    public Candidate updateCandidate(CandidateRequestDTO candidate, Long id) {
        Candidate existingCandidate = candidateRepo.findById(id).orElse(null);
        if (existingCandidate != null) {
            existingCandidate.setCandidateName(candidate.getCandidateName());
            existingCandidate.setParty(candidate.getParty());
            existingCandidate.setAdharCardNo(candidate.getAdharCardNo());
            return candidateRepo.save(existingCandidate);
        }
        return null;
    }

    @Transactional
    public String deleteCandidate(Long id) {
        Candidate candidate = candidateRepo.findById(id).orElse(null);
        boolean flag = false;
        if (candidateRepo.existsById(id)) {
            flag = voterClient.detachVotesFromCandidate(id, currentAuthorizationHeader());  // forward bearer
        }
//        System.out.println(flag);
        if(flag) {
            candidateRepo.delete(candidate);
            return "Candidate with ID " + id + " has been deleted.";
        }else {
            throw new RuntimeException("Candidate with " + id + " Failed to delete from Voter Service");
        }
    }

    public void putVoteCount(Long candidateId) {
        Candidate candidate = candidateRepo.findById(candidateId).orElse(null);
        if (candidate != null) {
            candidate.setVoteCount(candidate.getVoteCount() + 1);
            candidateRepo.save(candidate);
        } else {
            throw new ResourceNotFoundException("Candidate with ID " + candidateId + " not found.");
        }
    }

    public void removeVoteCount(Long candidateId) {
        Candidate candidate = candidateRepo.findById(candidateId).orElse(null);
        if (candidate != null) {
            candidate.setVoteCount(candidate.getVoteCount() - 1);
            candidateRepo.save(candidate);
        } else {
            throw new ResourceNotFoundException("Candidate with ID " + candidateId + " not found.");
        }
    }

//    public List<DisplayVotingDTO> display(){
//        List<Candidate> candidates = candidateRepo.findAll();
//        List<DisplayVotingDTO> displayVotingDTOList = new ArrayList<>();
//        for (Candidate candidate : candidates) {
//            System.out.println("fetching the details ");
//            List<VoterClientResposeDTO> voterClientResposeListDTO =  voterClient.getVoterfromVoter(candidate.getCandidateId(), currentAuthorizationHeader());
//            for(VoterClientResposeDTO voterClientResposeDTO:voterClientResposeListDTO){
//
//                DisplayVotingDTO temp = new DisplayVotingDTO();
//                temp.setVoterName(voterClientResposeDTO.getVoterName());
//                temp.setHasVoted(voterClientResposeDTO.getHasVoted());
//                if(voterClientResposeDTO.getHasVoted().equals("No")){
//                    temp.setCandidateName(null);
//                    temp.setParty(null);
//                }
//                else{
//                    temp.setCandidateName(candidate.getCandidateName());
//                    temp.setParty(candidate.getParty());
//                }
//
//
//                displayVotingDTOList.add(temp);
//
//            }
//        }
//        System.out.println("IN Candidate service and your DTO is "+displayVotingDTOList);
//
//        return displayVotingDTOList;
//    }

    public List<com.Sura.CandidateService.DTO.DisplayVotingDTO> display() {
        List<Candidate> candidates = candidateRepo.findAll();
        List<com.Sura.CandidateService.DTO.DisplayVotingDTO> displayVotingDTOList = new ArrayList<>();

        for (Candidate candidate : candidates) {
            if (candidate.getVoteCount() > 0) {
                List<com.sura.generated.model.VoterClientResposeDTO> voterClientResposeListDTO =
                        voterClient.getVoterfromVoter(candidate.getCandidateId(), currentAuthorizationHeader());

                for (com.sura.generated.model.VoterClientResposeDTO voter : voterClientResposeListDTO) {
                    if ("Yes".equalsIgnoreCase(voter.getHasVoted())) {
                        com.Sura.CandidateService.DTO.DisplayVotingDTO dto = new com.Sura.CandidateService.DTO.DisplayVotingDTO(); // NEW INSTANCE per voter
                        dto.setCandidateName(candidate.getCandidateName());
                        dto.setParty(candidate.getParty());
                        dto.setVoterName(voter.getVoterName());
                        dto.setHasVoted(voter.getHasVoted());
                        displayVotingDTOList.add(dto);
                    }
                }
            }
        }
        return displayVotingDTOList;
    }


    public List<com.Sura.CandidateService.DTO.DisplayVotingDTO> displaywithCandidateid(Long candidateId) {
        Candidate candidate = candidateRepo.findById(candidateId).orElse(null);
        if(candidate == null){
            throw new ResourceNotFoundException("Candidate with ID " + candidateId + " not found.");
        }
        List<com.Sura.CandidateService.DTO.DisplayVotingDTO> displayVotingDTOList = new ArrayList<>();

        List<com.sura.generated.model.VoterClientResposeDTO> voterClientResposeListDTO =voterClient.getVoterfromVoter(candidate.getCandidateId(), currentAuthorizationHeader());
        com.Sura.CandidateService.DTO.DisplayVotingDTO dto = new DisplayVotingDTO();
        dto.setCandidateName(candidate.getCandidateName());
        dto.setParty(candidate.getParty());
        for (VoterClientResposeDTO voter : voterClientResposeListDTO) {
            dto.setVoterName(voter.getVoterName());
            dto.setHasVoted(voter.getHasVoted());
            // Only add if the voter has actually voted
            if ("Yes".equalsIgnoreCase(voter.getHasVoted())) {
                displayVotingDTOList.add(dto);
            }
        }


        return displayVotingDTOList;
    }



    public String electionResult(){
        List<Candidate> candidates = candidateRepo.findAllByOrderByVoteCountDesc();
        if(candidates.isEmpty()){
            throw new ResourceNotFoundException("Candidate Table is Empty");
        }
        return "The winner of the Election is " + candidates.get(0).getCandidateName() + " from " + candidates.get(0).getParty() + " with " + candidates.get(0).getVoteCount() + " votes.";
    }

    private String currentAuthorizationHeader() {
        var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return null;
        return attrs.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
    }
}