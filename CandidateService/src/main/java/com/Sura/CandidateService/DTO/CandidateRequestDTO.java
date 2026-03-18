package com.Sura.CandidateService.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateRequestDTO {

    @Schema(description = "id of the candidate", example = "123", required = true)
    @NotNull(message = "Id must not be null")
    private Long id;

    @Schema(description = "Full name of the candidate", example = "Rahul Sharma", required = true)
    @Size(min = 2, message = "Candidate name must have at least 2 characters")
    @NotBlank(message = "Candidate name cannot be blank")
    private String candidateName;

    @Schema(description = "Aadhar card number of the candidate", example = "123456789012", required = true)
    @NotNull
    private Long adharCardNo;

    @Schema(description = "Political party name of the candidate", example = "National Party", required = true)
    @NotBlank(message = "Party name cannot be blank")
    private String party;
}