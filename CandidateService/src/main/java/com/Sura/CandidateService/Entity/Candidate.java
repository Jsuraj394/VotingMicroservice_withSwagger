package com.Sura.CandidateService.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long candidateId;


    @NotNull
    private Long adharCardNo;

    @Size(min=2, message="Candidate name must have at least 2 characters")
    @NotBlank(message="Candidate name cannot be blank")
    private String candidateName;

    @NotBlank(message="Party name cannot be blank")
    private String party;
    @JsonIgnore
    @Column(nullable = false)
    private Integer voteCount=0;
}
