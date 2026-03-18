package com.Sura.VoterService.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Voter {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
//    @Column(name = "voterId")
    private Long voterId;

    @NotNull
    private Long adharCardNo;


    @NotBlank(message="Voter name cannot be blank")
    private String voterName;

    @NotNull(message="Email cannot be null")
    @Email(message="Email should be valid")
    private String voterEmail;

    private Boolean hasVoted=false;

    private Long candidateId;

}
