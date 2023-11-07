package com.example.b1esimageweb.Exceptions;

public class GalleryNotFoundException extends RuntimeException{
    public GalleryNotFoundException(String message){
        super(message);
    }
}
