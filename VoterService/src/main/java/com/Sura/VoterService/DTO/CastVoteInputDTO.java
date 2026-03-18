package com.Sura.VoterService.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CastVoteInputDTO {

    @Schema(description = "Unique ID of the voter casting the vote", example = "101",required = true)
    @NotNull(message = "Voter ID cannot be null")
    private Long voterId;

    @Schema(description = "Unique ID of the candidate receiving the vote",  example = "202", required = true)
    @NotNull(message = "Candidate ID cannot be null")
    private Long candidateId;
}
