package tg.togo.auth.securite;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class ServiceJwt {

  private final SecretKey cle;
  private final int expirationMinutes;

  public ServiceJwt(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.expirationMinutes}") int expirationMinutes
  ) {
    this.cle = Keys.hmacShaKeyFor(secret.getBytes());
    this.expirationMinutes = expirationMinutes;
  }

  public String genererToken(String utilisateurId) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(expirationMinutes * 60L);

    return Jwts.builder()
        .subject(utilisateurId) // subject = email
        .issuedAt(Date.from(now))
        .expiration(Date.from(exp))
        .signWith(cle)
        .compact();
  }

  public String validerEtLireSubject(String token) {
    return Jwts.parser()
        .verifyWith(cle)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }
}
