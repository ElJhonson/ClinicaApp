package com.clinica.exceptions;

public class CitaNotFoundException extends RuntimeException{
    public CitaNotFoundException(String message) {
        super(message);
    }
}
