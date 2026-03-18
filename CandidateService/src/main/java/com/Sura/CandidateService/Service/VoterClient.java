package com.Sura.CandidateService.Service;

import com.Sura.CandidateService.DTO.VoterClientResposeDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class VoterClient {
    private final RestClient restClient;

    public VoterClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:9081/api/voter")
                .build();
    }

    public boolean detachVotesFromCandidate(Long candidateId, String authorization) {
        ResponseEntity<Void> response = restClient.post()
                .uri("/detach/{candidateId}", candidateId)
                .headers(h -> { if (authorization != null) h.set("Authorization", authorization); })
                .retrieve()
                .toBodilessEntity();

        return response.getStatusCode().is2xxSuccessful();
    }


    public List<com.sura.generated.model.VoterClientResposeDTO> getVoterfromVoter(Long candidateid, String authorization){
        ResponseEntity<List<com.sura.generated.model.VoterClientResposeDTO>> response = restClient.get()
                .uri("/getVoterByCandidateId/{candidateid}", candidateid)
                .headers(h -> { if (authorization != null) h.set("Authorization", authorization); })
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<com.sura.generated.model.VoterClientResposeDTO>>() {});

        return response.getBody();
    }
}