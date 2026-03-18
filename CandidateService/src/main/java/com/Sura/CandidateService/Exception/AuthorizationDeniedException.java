package com.Sura.CandidateService.Exception;


import org.springframework.security.access.AccessDeniedException;

public class AuthorizationDeniedException extends AccessDeniedException {
    public AuthorizationDeniedException(String msg) {
        super(msg);
    }
}

