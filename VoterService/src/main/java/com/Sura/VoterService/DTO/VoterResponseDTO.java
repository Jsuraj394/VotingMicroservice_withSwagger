package com.Sura.VoterService.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoterResponseDTO {

    @Schema(description = "Unique ID of the voter", example = "101")
    private Long voterId;

    @Schema(description = "Full name of the voter",  example = "Rahul Sharma",   required = true)
    @NotBlank(message = "Voter name cannot be blank")
    private String voterName;

    @Schema(description = "Aadhar card number of the voter",example = "123456789012",required = true)
    @NotNull
    private Long adharCardNo;

    @Schema(description = "Email address of the voter",  example = "rahul.sharma@example.com", required = true)
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String voterEmail;

    @Schema(description = "Whether the voter has cast their vote", example = "true")
    private Boolean hasVoted = false;
}
