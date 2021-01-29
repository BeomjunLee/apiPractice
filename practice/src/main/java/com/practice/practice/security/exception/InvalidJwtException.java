package com.practice.practice.security.exception;

public class InvalidJwtException extends RuntimeException{

    public InvalidJwtException(String msg) {
        super(msg);
    }
}
