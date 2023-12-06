package com.example.b1esimageweb.Exceptions;

public class UnauthorizedAlbumDeletionException extends  RuntimeException{
    public UnauthorizedAlbumDeletionException(String message) {
        super(message);
    }
}
