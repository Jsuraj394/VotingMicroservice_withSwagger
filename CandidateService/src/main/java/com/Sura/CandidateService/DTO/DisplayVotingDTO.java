package com.Sura.CandidateService.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisplayVotingDTO {

    @Schema(description = "Name of the voter", example = "Rahul Sharma", required = true)
    private String voterName;

    @Schema(description = "Voting status of the voter (Yes/No)", example = "Yes", required = true)
    private String hasVoted;

    @Schema(description = "Name of the candidate voted for",   example = "Anita Desai",   required = true)
    private String candidateName;

    @Schema(description = "Political party of the candidate", example = "National Party", required = true)
    private String party;
}

