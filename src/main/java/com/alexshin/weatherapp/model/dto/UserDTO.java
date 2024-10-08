package com.alexshin.weatherapp.model.dto;


import com.alexshin.weatherapp.exception.parsing.PasswordsDoNotMatchException;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public final class UserDTO {
    Long id;
    String login;
    String password;

    public UserDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserDTO(String login, String password, String passRepeat) {
        if (!password.equals(passRepeat)) {
            throw new PasswordsDoNotMatchException();
        }
        this.login = login;
        this.password = password;
    }



}

