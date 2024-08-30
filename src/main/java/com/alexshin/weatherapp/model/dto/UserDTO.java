package com.alexshin.weatherapp.model.dto;


import com.alexshin.weatherapp.exception.parsing.PasswordsDoNotMatchException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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


//public record UserDTO (Long id, String login, String password) {
//}
