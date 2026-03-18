package com.Sura.CandidateService.Exception;

public class VoteNotAllowedException extends RuntimeException{
    public VoteNotAllowedException(String message) {
        super(message);
    }
}
