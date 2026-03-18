package com.Sura.CandidateService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoterClientResposeDTO {

    @Schema(description = "Name of the voter",example = "Rahul Sharma",required = true)
    private String voterName;

    @Schema(description = "Voting status of the voter (Yes/No)", example = "Yes",required = true)
    private String hasVoted;
}
