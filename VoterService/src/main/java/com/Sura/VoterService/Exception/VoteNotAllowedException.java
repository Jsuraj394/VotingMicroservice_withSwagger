package com.Sura.VoterService.Exception;

public class VoteNotAllowedException extends RuntimeException{
    public VoteNotAllowedException(String message) {
        super(message);
    }
}
