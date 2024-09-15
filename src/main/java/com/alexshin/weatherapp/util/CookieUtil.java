package com.alexshin.weatherapp.util;

import com.alexshin.weatherapp.model.dto.UserSessionDTO;
import com.alexshin.weatherapp.model.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;

public final class CookieUtil {
    private static final String salt = BCrypt.gensalt();

    private CookieUtil() {
        throw new UnsupportedOperationException("CookieUtil cannot be instantiated");
    }

    public static Optional<String> extractSessionCookie(HttpServletRequest req) {
        if (req.getCookies() == null){
            return Optional.empty();
        }
        return Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals("SessionId"))
                .findAny()
                .map(Cookie::getValue);

    }

    public static void setSessionCookie(HttpServletResponse resp, UserSessionDTO session, String path){
        resp.addCookie(createSessionCookie(session, path));
    }

    public static void deleteSessionCookie(HttpServletResponse resp, String path){
        Cookie cookie = new Cookie("SessionId", "");
        cookie.setMaxAge(0);
        cookie.setPath(path);
        resp.addCookie(cookie);
    }

    private static Cookie createSessionCookie(UserSessionDTO session, String path) {
        Cookie cookie = new Cookie("SessionId", session.getId());
        long age = ChronoUnit.HOURS.between(LocalDateTime.now(), session.getExpiresAt());
        cookie.setMaxAge((int) age * 3600);
        cookie.setPath(path);

        return cookie;
    }


}
