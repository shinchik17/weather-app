package com.alexshin.weatherapp.exception;

public class BaseRepositoryException extends RuntimeException{
    public BaseRepositoryException() {
        super();
    }

    public BaseRepositoryException(String message) {
        super(message);
    }

    public BaseRepositoryException(Throwable cause) {
        super(cause);
    }
}
