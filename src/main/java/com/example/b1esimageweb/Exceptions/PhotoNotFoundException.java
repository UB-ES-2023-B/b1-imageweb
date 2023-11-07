package com.example.b1esimageweb.Exceptions;

public class PhotoNotFoundException extends RuntimeException{
    public PhotoNotFoundException(String message){
        super(message);
    }
}
