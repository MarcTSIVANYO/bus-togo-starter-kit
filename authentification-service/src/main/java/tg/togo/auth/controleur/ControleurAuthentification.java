package tg.togo.auth.controleur;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tg.togo.auth.dto.DtosAuth.DemandeConnexion;
import tg.togo.auth.dto.DtosAuth.DemandeInscription;
import tg.togo.auth.dto.DtosAuth.ReponseToken;
import tg.togo.auth.securite.ServiceJwt;
import tg.togo.auth.service.StockageUtilisateurs;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ControleurAuthentification {

  private final StockageUtilisateurs stockage;
  private final ServiceJwt jwt;

  @PostMapping("/inscription")
  public void inscription(@RequestBody DemandeInscription req) {
    if (req.getEmail() == null || req.getEmail().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email requis");
    }
    if (stockage.trouverParEmail(req.getEmail()) != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email déjà utilisé");
    }

    StockageUtilisateurs.Utilisateur u = new StockageUtilisateurs.Utilisateur();
    u.setEmail(req.getEmail());
    u.setMotDePasse(req.getMotDePasse());
    u.setNomComplet(req.getNomComplet());
    stockage.sauvegarder(u);
  }

  @PostMapping("/connexion")
  public ReponseToken connexion(@RequestBody DemandeConnexion req) {
    var u = stockage.trouverParEmail(req.getEmail());
    if (u == null || !u.getMotDePasse().equals(req.getMotDePasse())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants invalides");
    }
    return new ReponseToken(jwt.genererToken(u.getEmail()));
  }

  // Endpoint utilitaire (option) : aide pour vérifier token côté dev
  @GetMapping("/moi")
  public String moi(@RequestHeader("Authorization") String authorization) {
    String token = authorization.replace("Bearer ", "");
    return jwt.validerEtLireSubject(token);
  }
}
