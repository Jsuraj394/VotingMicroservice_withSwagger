package com.Sura.VoterService.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CandidateClient {

    private final RestClient restClient;

    public CandidateClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:8080/api/candidate")
                .build();
    }

    public boolean addVoteToCandidate(Long candidateId, String authorization) {
        ResponseEntity<Void> response = restClient.post()
                .uri("/vote/{candidateId}", candidateId)
                .headers(h -> { if (authorization != null) h.set("Authorization", authorization); })
                .retrieve()
                .toBodilessEntity();

        return response.getStatusCode().is2xxSuccessful();
    }

    public boolean removeVoteFromCandidate(Long candidateId, String authorization) {
        System.out.println("Removing vote from candidate with ID " + candidateId);
        ResponseEntity<Void> response = restClient.post()
                .uri("/removeVote/{candidateId}", candidateId)
                .headers(h -> { if (authorization != null) h.set("Authorization", authorization); })
                .retrieve()
                .toBodilessEntity();
        System.out.println("Response status code: " + response.getStatusCode());
        return response.getStatusCode().is2xxSuccessful();
    }
}