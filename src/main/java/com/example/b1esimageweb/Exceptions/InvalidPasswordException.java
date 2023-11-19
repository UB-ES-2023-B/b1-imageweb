package com.example.b1esimageweb.Exceptions;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String message){
        super(message);
    }
}
