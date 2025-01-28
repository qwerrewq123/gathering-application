package spring.myproject.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.nio.charset.StandardCharsets;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtProvider {


    @Value("${secret-key}")
    private String secretKey;

    public String create(String userId){

        Date expiredDate = Date.from(Instant.now().plus(10, ChronoUnit.HOURS));

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)) ;

        String jwt = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(userId).setIssuedAt(new Date()).setExpiration(expiredDate)
                .compact();

        return jwt;
    }

    public String validate(String jwt){

        Claims claims = null;
        String subject =null;

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)) ;


        try{

            claims = Jwts.parserBuilder().setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt).getBody();

            subject = claims.getSubject();


        } catch(Exception exception){
            exception.printStackTrace();
            return null;

        }

        return subject;


    }

}
