package com.Sura.CandidateService.Controller;



import com.Sura.CandidateService.Entity.Candidate;
import com.Sura.CandidateService.Service.CandidateService;
import com.sura.generated.model.CandidateIdDTO;
import com.sura.generated.model.CandidateRequestDTO;
import com.sura.generated.model.CandidateResponseDTO;
import com.sura.generated.model.DisplayVotingDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/candidate/")
@Tag(name="Candidate API's")
public class CandidateController {
    private final CandidateService candidateService;

    @Autowired
    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @Operation(summary = "Register a new candidate", description = "Registers a candidate with initial vote count set to 0")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Candidate registered successfully", content = @Content(schema = @Schema(implementation = CandidateResponseDTO.class)))
    })
    @PostMapping("register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CandidateResponseDTO> registerCandidate(@Valid @RequestBody Candidate candidate){
        candidate.setVoteCount(0);
        Candidate candidateResponse = candidateService.registerCandidate(candidate);
        CandidateResponseDTO candidateResponseDTO = new CandidateResponseDTO();
        candidateResponseDTO.setCandidateId( candidateResponse.getCandidateId());
        candidateResponseDTO.setCandidateName( candidateResponse.getCandidateName());
        candidateResponseDTO.setAdharCardNo( candidateResponse.getAdharCardNo());
        candidateResponseDTO.setParty( candidateResponse.getParty());

        return new ResponseEntity<>(candidateResponseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get candidate by ID", description = "Fetch candidate details by candidate ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidate found",content = @Content(schema = @Schema(implementation = CandidateResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Candidate not found")
    })
    @GetMapping("getCandidateById")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<CandidateResponseDTO> getCandidateById(@RequestBody CandidateIdDTO getCandidateByIDDTO){
        Candidate candidateResponse = candidateService.getCandidateById(getCandidateByIDDTO.getId());
        CandidateResponseDTO candidateResponseDTO = new CandidateResponseDTO();
        candidateResponseDTO.setCandidateId( candidateResponse.getCandidateId());
        candidateResponseDTO.setCandidateName( candidateResponse.getCandidateName());
        candidateResponseDTO.setAdharCardNo( candidateResponse.getAdharCardNo());
        candidateResponseDTO.setParty( candidateResponse.getParty());

        return new ResponseEntity<>(candidateResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get all candidates", description = "Fetch list of all registered candidates")
    @ApiResponse(responseCode = "200", description = "List of candidates",content = @Content(schema = @Schema(implementation = CandidateResponseDTO.class)))
    @GetMapping("getAllCandidates")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<CandidateResponseDTO>> getAllCandidates() {
        List<Candidate> candidateResponse = candidateService.getAllCandidates();
        List<CandidateResponseDTO> candidateResponseDTOList = new ArrayList<>();
        for(Candidate candidate : candidateResponse) {

            CandidateResponseDTO candidateResponseDTO =  new CandidateResponseDTO();

            candidateResponseDTO.setCandidateId( candidate.getCandidateId());
            candidateResponseDTO.setCandidateName( candidate.getCandidateName());
            candidateResponseDTO.setAdharCardNo( candidate.getAdharCardNo());
            candidateResponseDTO.setParty( candidate.getParty());

            candidateResponseDTOList.add(candidateResponseDTO);
        }
        return new ResponseEntity<>(candidateResponseDTOList, HttpStatus.OK);
    }

    @Operation(summary = "Update candidate", description = "Update candidate details by ID")
    @ApiResponse(responseCode = "202", description = "Candidate updated successfully",  content = @Content(schema = @Schema(implementation = CandidateResponseDTO.class)))
    @PutMapping("updateCandidate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CandidateResponseDTO> updateCandidate(@Valid @RequestBody CandidateRequestDTO candidateRequestDTO){
        Candidate candidateResponse = candidateService.updateCandidate(candidateRequestDTO, candidateRequestDTO.getId());
        CandidateResponseDTO candidateResponseDTO = new CandidateResponseDTO();
        candidateResponseDTO.setCandidateId( candidateResponse.getCandidateId());
        candidateResponseDTO.setCandidateName( candidateResponse.getCandidateName());
        candidateResponseDTO.setAdharCardNo( candidateResponse.getAdharCardNo());
        candidateResponseDTO.setParty( candidateResponse.getParty());

        return new ResponseEntity<>(candidateResponseDTO, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Delete candidate", description = "Delete candidate by ID")
    @ApiResponse(responseCode = "200", description = "Candidate deleted successfully")
    @DeleteMapping("deleteCandidate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCandidate(@RequestBody com.sura.generated.model.CandidateIdDTO getCandidateByIDDTO) {
        return new ResponseEntity<>(candidateService.deleteCandidate(getCandidateByIDDTO.getId()), HttpStatus.OK);
    }

    @Operation(summary = "Vote for candidate", description = "Increment vote count for candidate by ID")
    @ApiResponse(responseCode = "200", description = "Vote cast successfully")
    @PostMapping("vote/{candidateId}")
    public ResponseEntity<String> voteForCandidate(@PathVariable("candidateId") Long candidateId) {
        try {
            candidateService.putVoteCount(candidateId);
            return new ResponseEntity<>("Vote cast successfully for candidate with ID " + candidateId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to cast vote: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Remove vote for candidate", description = "Decrement vote count for candidate by ID")
    @ApiResponse(responseCode = "200", description = "Vote removed successfully")
    @PostMapping("removeVote/{candidateId}")
    public ResponseEntity<String> removeVoteForCandidate(@PathVariable("candidateId") Long candidateId) {
        try {
            candidateService.removeVoteCount(candidateId);
            return new ResponseEntity<>("Vote removed successfully for candidate with ID " + candidateId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to remove vote: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get election result", description = "Fetch election result summary")
    @ApiResponse(responseCode = "200", description = "Election result string")
    @GetMapping("electionResult")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> getElectionResult() {
        return new ResponseEntity<>(candidateService.electionResult(),HttpStatus.OK);
    }

    @Operation(summary = "Display candidates and voters", description = "Fetch combined candidate and voter details")
    @ApiResponse(responseCode = "200", description = "List of candidates and voters",content = @Content(schema = @Schema(implementation = DisplayVotingDTO.class)))
    @GetMapping("displayCandidateAndVoter")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<com.Sura.CandidateService.DTO.DisplayVotingDTO>> displayBothTable() {
        return new ResponseEntity<>(candidateService.display(),HttpStatus.OK);
    }

    //    displaywithCandidateid
    @Operation(summary = "Display candidates and voters", description = "Fetch combined voter details for Candidate")
    @ApiResponse(responseCode = "200", description = "List of candidates and voters",content = @Content(schema = @Schema(implementation = com.Sura.CandidateService.DTO.DisplayVotingDTO.class)))
    @GetMapping("displayCandidateAndVoterForCandidate")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<com.Sura.CandidateService.DTO.DisplayVotingDTO>> displayBothTableViaCandidateId(@RequestBody CandidateIdDTO candidateIdDTO) {
        return new ResponseEntity<>(candidateService.displaywithCandidateid(candidateIdDTO.getId()),HttpStatus.OK);
    }
}
