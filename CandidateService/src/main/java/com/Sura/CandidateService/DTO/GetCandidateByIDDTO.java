package com.Sura.CandidateService.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCandidateByIDDTO {
    @NotNull(message = "Id must not be Null")
    private Long id;

}
