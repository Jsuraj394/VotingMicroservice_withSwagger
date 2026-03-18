package com.Sura.VoterService.Controller;

import com.Sura.VoterService.DTO.CastVoteInputDTO;
import com.Sura.VoterService.DTO.VoterIdDTO;
import com.Sura.VoterService.DTO.VoterResponseDTO;
import com.Sura.VoterService.DTO.VoterResponseForCandidateDTO;
import com.Sura.VoterService.Entity.Voter;
import com.Sura.VoterService.Service.VoterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/voter/")
@Tag(name="Voter API's")
public class VoterController {

    private final VoterService voterService;

    @Autowired
    public VoterController(VoterService voterService) {
        this.voterService = voterService;
    }

    @Operation(summary = "Register a new voter", description = "Registers a voter and returns voter details")
    @ApiResponse(responseCode = "201", description = "Voter registered successfully", content = @Content(schema = @Schema(implementation = VoterResponseDTO.class)))

    @PostMapping("register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<VoterResponseDTO> registerVoter(@Valid @RequestBody Voter voter){
        Voter response_voter = voterService.registerVoter(voter);
        VoterResponseDTO voterResponseDTO = new VoterResponseDTO();
        voterResponseDTO.setVoterId(response_voter.getVoterId());
        voterResponseDTO.setAdharCardNo(response_voter.getAdharCardNo());
        voterResponseDTO.setVoterName(response_voter.getVoterName());
        voterResponseDTO.setVoterEmail(response_voter.getVoterEmail());
        voterResponseDTO.setHasVoted(response_voter.getHasVoted());
        return new ResponseEntity<>(voterResponseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all voters", description = "Fetch list of all registered voters")
    @ApiResponse(responseCode = "200", description = "List of voters",  content = @Content(schema = @Schema(implementation = VoterResponseDTO.class)))
    @GetMapping("all")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<VoterResponseDTO>> getAllVoters(){
        List<Voter> voters = voterService.getAllVoters();
        List<VoterResponseDTO> voterResponseDTOs = new ArrayList<>();
        for(Voter voter : voters){
            VoterResponseDTO voterResponseDTO = new VoterResponseDTO();
            voterResponseDTO.setVoterId(voter.getVoterId());
            voterResponseDTO.setVoterName(voter.getVoterName());
            voterResponseDTO.setAdharCardNo(voter.getAdharCardNo());
            voterResponseDTO.setVoterEmail(voter.getVoterEmail());
            voterResponseDTO.setHasVoted(voter.getHasVoted());
            voterResponseDTOs.add(voterResponseDTO);
        }
        return new ResponseEntity<>(voterResponseDTOs, HttpStatus.OK);
    }

    @Operation(summary = "Get voter by ID", description = "Fetch voter details by voter ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voter found", content = @Content(schema = @Schema(implementation = VoterResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Voter not found")
    })
    @GetMapping("getVoterById")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<VoterResponseDTO> getVoterById(@RequestBody VoterIdDTO voterIdDTO){
        Voter response_voter = voterService.getVoterById(voterIdDTO.getId());
        VoterResponseDTO voterResponseDTO = new VoterResponseDTO();
        voterResponseDTO.setVoterId(response_voter.getVoterId());
        voterResponseDTO.setVoterName(response_voter.getVoterName());
        voterResponseDTO.setAdharCardNo(response_voter.getAdharCardNo());
        voterResponseDTO.setVoterEmail(response_voter.getVoterEmail());
        voterResponseDTO.setHasVoted(response_voter.getHasVoted());
        return new ResponseEntity<>(voterResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Delete voter", description = "Delete voter by ID")
    @ApiResponse(responseCode = "200", description = "Voter deleted successfully")
    @DeleteMapping("delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteVoter(@RequestHeader(name = "Authorization", required = false) String authorization, @RequestBody VoterIdDTO voterIdDTO) {
        return new ResponseEntity<>(voterService.deleteVoter(voterIdDTO.getId(), authorization), HttpStatus.OK);
    }

    @Operation(summary = "Update voter", description = "Update voter details by ID")
    @ApiResponse(responseCode = "200", description = "Voter updated successfully", content = @Content(schema = @Schema(implementation = VoterResponseDTO.class)))
    @PutMapping("update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VoterResponseDTO> updateVoter( @Valid @RequestBody Voter voter){
//        voter.setVoterId(Id);
        Voter response_voter = voterService.updateVoter(voter, voter.getVoterId());
        VoterResponseDTO voterResponseDTO = new VoterResponseDTO();
        voterResponseDTO.setVoterId(response_voter.getVoterId());
        voterResponseDTO.setVoterName(response_voter.getVoterName());
        voterResponseDTO.setAdharCardNo(response_voter.getAdharCardNo());
        voterResponseDTO.setVoterEmail(response_voter.getVoterEmail());
        voterResponseDTO.setHasVoted(response_voter.getHasVoted());
        return new ResponseEntity<>(voterResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Detach voters from candidate", description = "Detach all voters associated with a candidate")
    @ApiResponse(responseCode = "200", description = "Voters detached successfully")
    @PostMapping("detach/{candidateId}")
    public void detachVoterFromCandidate(@PathVariable("candidateId") Long candidateId) {
        voterService.detachVoterFromCandidate(candidateId);
    }

    @Operation(summary = "Get voters by candidate ID", description = "Fetch voters who voted for a candidate")
    @ApiResponse(responseCode = "200", description = "List of voters for candidate", content = @Content(schema = @Schema(implementation = VoterResponseForCandidateDTO.class)))
    @GetMapping("getVoterByCandidateId/{candidateid}")
    public List<VoterResponseForCandidateDTO> getVoterByName(@PathVariable("candidateid") Long candidateId){
        return voterService.getVoteByCandidateID(candidateId);
    }

    @Operation(summary = "Cast vote", description = "Cast a vote for a candidate by voter ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vote cast successfully"),
            @ApiResponse(responseCode = "403", description = "Vote casting failed")
    })
    @PostMapping("castVote/")
    public ResponseEntity<String> castVote( @RequestHeader(name = "Authorization", required = false) String authorization,  @Valid @RequestBody CastVoteInputDTO castVoteInputDTO ) {
        try {
            return new ResponseEntity<>(voterService.castVote(castVoteInputDTO.getVoterId(), castVoteInputDTO.getCandidateId(), authorization),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to cast vote for voter with ID " + castVoteInputDTO.getVoterId()  + " and candidate with ID " + castVoteInputDTO.getCandidateId() + ": " + e.getMessage(), HttpStatus.FORBIDDEN );
        }
    }
}