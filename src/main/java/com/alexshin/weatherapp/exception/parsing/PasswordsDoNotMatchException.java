package com.alexshin.weatherapp.exception.parsing;

public class PasswordsDoNotMatchException extends IllegalArgumentException {

    public PasswordsDoNotMatchException() {
        super("Passwords do not match");
    }

}
