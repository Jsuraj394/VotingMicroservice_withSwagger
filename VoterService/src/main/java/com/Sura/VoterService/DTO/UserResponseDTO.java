package com.Sura.VoterService.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    @Schema(description = "Unique ID of the user", example = "1")
    private Long id;

    @Schema(description = "Username chosen by the user", example = "john_doe")
    private String username;

    @Schema(description = "Password of the user (usually hashed)",  example = "password123", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;
}

