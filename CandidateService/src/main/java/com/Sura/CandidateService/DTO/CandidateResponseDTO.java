package com.Sura.CandidateService.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateResponseDTO {

    @Schema(description = "Unique ID of the candidate", example = "101")
    private Long candidateId;

    @Schema(description = "Full name of the candidate", example = "Rahul Sharma", required = true)
    @NotBlank(message = "Candidate name cannot be blank")
    private String candidateName;

    @Schema(description = "Aadhar card number of the candidate", example = "123456789012",  required = true)
    @NotNull
    private Long adharCardNo;

    @Schema(description = "Political party name of the candidate", example = "National Party", required = true)
    @NotBlank(message = "Party name cannot be blank")
    private String party;

    // getters and setters
}