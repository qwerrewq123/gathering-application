package spring.myproject.common.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.nio.charset.StandardCharsets;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtProvider {


    private final  String secretKey;
    private final int accessExpiration;
    private final int refreshExpiration;
    private final Key SECRET_KEY;

    public JwtProvider(@Value("${jwt.secretKey}") String secretKey,
                       @Value("${jwt.access.expiration}") int accessExpiration,
                       @Value("${jwt.refresh.expiration}") int refreshExpiration) {
        this.secretKey = secretKey;
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
        this.SECRET_KEY = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS512.getJcaName());
    }


    public String createAccessToken(String username, String role){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+accessExpiration*60*1000L))
                .signWith(SECRET_KEY)
                .compact();
        return token;
    }

    public String createRefreshToken(String username, String role){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+refreshExpiration*60*1000L))
                .signWith(SECRET_KEY)
                .compact();
        return token;
    }


}
