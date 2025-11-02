package com.clinica.exceptions;

public class PsicologoNotFoundException extends RuntimeException{
    public PsicologoNotFoundException(String message) {
        super(message);
    }
}
