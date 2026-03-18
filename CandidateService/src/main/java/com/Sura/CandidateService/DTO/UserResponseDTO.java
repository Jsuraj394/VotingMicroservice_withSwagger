package com.Sura.CandidateService.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    @Schema(description = "Unique ID of the user", example = "1")
    private Long id;

    @Schema(description = "Username chosen by the user", example = "john_doe")
    private String username;

    @Schema(description = "Password of the user (hashed or plain depending on implementation)", example = "password123")
    private String password;


}
