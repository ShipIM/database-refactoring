package com.example.utils;

import com.example.model.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for working with JWT (JSON Web Tokens).
 * Provides methods for generating, parsing, and validating JWT tokens.
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String access;

    /**
     * Generates an access token (JWT) for a given user with additional claims.
     *
     * @param extraClaims additional claims to include in the token
     * @param user        the user for whom the token is generated
     * @return a JWT token as a String
     */
    public String generateAccessToken(Map<String, Object> extraClaims, User user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .signWith(getSignInKey(access), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Converts a base64 encoded string into a secret key.
     *
     * @param key the base64 encoded secret key string
     * @return a Key object used for signing the token
     */
    public Key getSignInKey(String key) {
        var keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts the username (subject) from the access token.
     *
     * @param token the JWT token
     * @return the username from the token
     */
    public String extractAccessUsername(String token) {
        return extractUsername(token, access);
    }

    /**
     * Extracts the claim (subject) from the JWT token using a specific key.
     *
     * @param token the JWT token
     * @param key   the key used to validate the token
     * @return the claim (subject) from the token
     */
    private String extractUsername(String token, String key) {
        return extractClaim(token, key, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param token          the JWT token
     * @param key            the key used to validate the token
     * @param claimsResolver a function to extract the desired claim
     * @param <T>            the type of the claim to extract
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, String key, Function<Claims, T> claimsResolver) {
        var claims = extractAllClaims(token, key);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token the JWT token
     * @param key   the key used to validate the token
     * @return the claims from the token
     */
    public Claims extractAllClaims(String token, String key) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey(key))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Validates whether the provided access token is valid.
     *
     * @param token the JWT token
     * @return true if the token is valid, false otherwise
     */
    public boolean isAccessTokenValid(String token) {
        return isTokenValid(token, access);
    }

    /**
     * Validates a JWT token using a specific key.
     *
     * @param token the JWT token
     * @param key   the key used to validate the token
     * @return true if the token is valid, false otherwise
     */
    private boolean isTokenValid(String token, String key) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignInKey(key))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("malformed jwt", mjEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

}
