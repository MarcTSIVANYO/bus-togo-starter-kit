package tg.togo.auth.service;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StockageUtilisateurs {
  private final Map<String, Utilisateur> utilisateurs = new ConcurrentHashMap<>();

  @Data
  public static class Utilisateur {
    private String email;
    private String motDePasse; // TP : en clair. Extension : BCrypt.
    private String nomComplet;
  }

  public Utilisateur trouverParEmail(String email) {
    return utilisateurs.get(email);
  }

  public void sauvegarder(Utilisateur u) {
    utilisateurs.put(u.getEmail(), u);
  }
}
