package com.project.ecomerce.security;

import com.project.ecomerce.client.entity.Client;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY = "secret-key-super-safe"; // depois vamos melhorar isso

    public String generateToken(Client client) {

        return Jwts.builder()
                .setSubject(client.getEmail())
                .claim("role", client.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}