package com.samit.securitybasic.service;

import com.samit.securitybasic.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

@Service
public class JwtService {

    private static final String jwtSecreteKey = "ddgdbydjsmsjjsmhdgdndjsksjbdddjdkddk";

    //generate a secret key method
    private SecretKey generateSecreteKey(){
        return Keys.hmacShaKeyFor(jwtSecreteKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(UserEntity user){
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email",user.getEmail())
                .claim("roles", Set.of("ADMIN","USER"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60))
                .signWith(generateSecreteKey())
                .compact();
    }

    public Long generateUserIdFromToken(String token){
        Claims claims =Jwts
                .parser()
                .verifyWith(generateSecreteKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        System.out.println("token email :: "+claims.get("email"));
        return Long.valueOf(claims.getSubject());
    }


}
