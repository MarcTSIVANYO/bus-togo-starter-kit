package tg.togo.reservation.controleur;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tg.togo.reservation.modele.Reservation;
import tg.togo.reservation.securite.LecteurJwtSimple;
import tg.togo.reservation.service.ServiceReservation;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ControleurReservation {

  private final ServiceReservation service;
  private final LecteurJwtSimple jwt;

  @PostMapping
  public Reservation creer(
      @RequestHeader("Authorization") String authorization,
      @RequestHeader("Idempotency-Key") String cleIdempotence,
      @RequestBody DemandeReservation req
  ) {
    String utilisateurId = jwt.extraireSubject(authorization);
    return service.creerEnAttente(utilisateurId, req.trajetId, req.numeroSiege, cleIdempotence);
  }

  // Endpoints internes (saga)
  @PostMapping("/{id}/confirmer")
  public Reservation confirmer(@PathVariable String id) {
    return service.confirmer(id);
  }

  @PostMapping("/{id}/annuler")
  public Reservation annuler(@PathVariable String id) {
    return service.annuler(id);
  }

  @Data
  public static class DemandeReservation {
    private String trajetId;
    private int numeroSiege;
  }
}
