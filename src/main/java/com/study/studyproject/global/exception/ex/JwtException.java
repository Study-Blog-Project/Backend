package com.study.studyproject.global.exception.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JwtException extends RuntimeException{
    public JwtException(String message) {
        super(message);
    }
}

