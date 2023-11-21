package com.example.b1esimageweb.Exceptions;

import java.io.IOException;

public class PhotoStorageException extends RuntimeException {
    public PhotoStorageException(String message) {
        super(message);
    }
}
