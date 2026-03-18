package com.Sura.VoterService.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String base64Secret; // SAME in both services

    @Value("${jwt.expiration-ms:1800000}") // default 30 minutes
    private long expirationMs;

    @Value("${jwt.issuer:sura-auth}")
    private String issuer;

    private SecretKey key() {
        // SECRET IS BASE64-ENCODED — decode once for signing & verifying
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
    }

    public String generateToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMs)))
                .signWith(key(), Jwts.SIG.HS256) // same key & alg for signing
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        Date exp = extractClaim(token, Claims::getExpiration);
        return username != null
                && username.equals(userDetails.getUsername())
                && exp != null
                && exp.after(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key())                 // same key & alg for verifying
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}