package tg.togo.reservation.securite;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;

/**
 * Lecture minimale du JWT (TP) :
 * - On lit le payload et on récupère "sub"
 * - On NE vérifie pas la signature ici (simplification S4).
 * Extension : appeler auth-service /api/auth/moi ou vérifier signature.
 */
@Component
public class LecteurJwtSimple {
  private final ObjectMapper mapper = new ObjectMapper();

  public String extraireSubject(String authorizationHeader) {
    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      throw new RuntimeException("Authorization Bearer requis");
    }
    String token = authorizationHeader.replace("Bearer ", "");
    String[] parts = token.split("\\.");
    if (parts.length < 2) throw new RuntimeException("JWT invalide");

    String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
    try {
      Map<?, ?> payload = mapper.readValue(payloadJson, Map.class);
      return (String) payload.get("sub");
    } catch (Exception e) {
      throw new RuntimeException("JWT illisible");
    }
  }
}
