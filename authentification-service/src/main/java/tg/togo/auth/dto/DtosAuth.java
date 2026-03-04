package tg.togo.auth.dto;

import lombok.Data;

public class DtosAuth {
  @Data
  public static class DemandeInscription {
    private String email;
    private String motDePasse;
    private String nomComplet;
  }

  @Data
  public static class DemandeConnexion {
    private String email;
    private String motDePasse;
  }

  @Data
  public static class ReponseToken {
    private String token;
    public ReponseToken(String token) { this.token = token; }
  }
}
