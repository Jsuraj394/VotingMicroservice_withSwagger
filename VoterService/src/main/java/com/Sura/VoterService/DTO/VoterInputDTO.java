package com.Sura.VoterService.DTO;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoterInputDTO {

    @Schema(description = "Unique ID of the voter",  example = "101", required = true)
    @NotNull(message = "Voter ID cannot be null")
    private Long voterId;

    @Schema(description = "Unique ID of the candidate",example = "202", required = true)
    @NotNull(message = "Candidate ID cannot be null")
    private Long candidateId;
}
