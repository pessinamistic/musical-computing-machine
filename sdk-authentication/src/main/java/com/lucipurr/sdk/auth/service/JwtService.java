/* (C) Lucipurr 69@420 */
package com.lucipurr.sdk.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${app.jwt.secret}")
  private String secret;

  @Value("${app.jwt.expiration-minutes}")
  private int expirationMinutes;

  public String generateToken(UserDetails userDetails) {
    Algorithm algorithm = Algorithm.HMAC256(secret);
    Instant now = Instant.now();
    Instant expiration = now.plus(expirationMinutes, ChronoUnit.MINUTES);

    return JWT.create()
        .withSubject(userDetails.getUsername())
        .withIssuedAt(Date.from(now))
        .withExpiresAt(Date.from(expiration))
        .sign(algorithm);
  }

  public String validateTokenAndGetUsername(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      JWTVerifier verifier = JWT.require(algorithm).build();
      DecodedJWT jwt = verifier.verify(token);
      return jwt.getSubject();
    } catch (JWTVerificationException exception) {
      return null;
    }
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = validateTokenAndGetUsername(token);
    return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    try {
      DecodedJWT jwt = JWT.decode(token);
      return jwt.getExpiresAt().before(new Date());
    } catch (Exception e) {
      return true;
    }
  }

  public String extractUsername(String token) {
    return validateTokenAndGetUsername(token);
  }

  //  public Duration getExpirationTime() {
  //    return Duration.of(expirationMinutes, ChronoUnit.MINUTES);
  //  }

  public Instant getExpirationTime() {
    return Instant.now().plus(expirationMinutes, ChronoUnit.MINUTES);
  }

  public Date getExpirationDate() {
    return Date.from(getExpirationTime());
  }
}
