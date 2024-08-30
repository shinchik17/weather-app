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
        return Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals("SessionId"))
                .findAny()
                .map(Cookie::getName);

    }

    public static void setSessionCookie(HttpServletResponse resp, UserSessionDTO session){
        resp.addCookie(createSessionCookie(session));
    }

    public static void deleteSessionCookie(HttpServletResponse resp){
        Cookie cookie = new Cookie("SessionId", "");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }

    private static Cookie createSessionCookie(UserSessionDTO session) {
        Cookie cookie = new Cookie("SessionId", session.getId());
        long age = ChronoUnit.HOURS.between(LocalDateTime.now(), session.getExpiresAt());
        cookie.setMaxAge((int) age * 3600);

        return cookie;
    }


}
