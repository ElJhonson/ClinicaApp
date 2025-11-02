package com.clinica.exceptions;

public class SecretariaNotFoundException extends RuntimeException{
    public SecretariaNotFoundException(String message) {
        super(message);
    }
}
