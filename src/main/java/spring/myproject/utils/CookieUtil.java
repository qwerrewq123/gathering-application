package spring.myproject.utils;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    public static Cookie getCookie(String name, String value,int expiration){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(expiration);
        return cookie;
    }
}
