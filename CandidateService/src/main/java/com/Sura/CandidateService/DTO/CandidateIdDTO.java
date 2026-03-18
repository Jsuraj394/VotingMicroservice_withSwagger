package com.Sura.CandidateService.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateIdDTO {
    @Schema(description = "candidate id ", example = "123", required = true)
    @NotNull(message = "Id must not be Null")
    private Long id;
}
