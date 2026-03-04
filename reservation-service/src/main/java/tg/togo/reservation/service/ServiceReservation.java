package tg.togo.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tg.togo.reservation.modele.Reservation;
import tg.togo.reservation.repository.ReservationRepository;
import tg.togo.reservation.repository.TrajetRepository;

@Service
@RequiredArgsConstructor
public class ServiceReservation {

  private final ReservationRepository reservationRepository;
  private final TrajetRepository trajetRepository;

  public Reservation creerEnAttente(String utilisateurId, String trajetId, int numeroSiege, String cleIdempotence) {
    // idempotence : même clé => même résultat
    var existante = reservationRepository.findByCleIdempotence(cleIdempotence);
    if (existante.isPresent()) return existante.get();

    var trajet = trajetRepository.findById(trajetId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trajet introuvable"));

    if (numeroSiege < 1 || numeroSiege > trajet.getNombreSiegesTotal()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Numero de siège invalide");
    }

    Reservation r = new Reservation();
    r.setUtilisateurId(utilisateurId);
    r.setTrajetId(trajetId);
    r.setNumeroSiege(numeroSiege);
    r.setStatut("EN_ATTENTE");
    r.setCleIdempotence(cleIdempotence);

    try {
      return reservationRepository.save(r);
    } catch (DataIntegrityViolationException e) {
      // conflit siège déjà pris
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Siège déjà réservé");
    }
  }

  public Reservation confirmer(String reservationId) {
    Reservation r = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Réservation introuvable"));
    if (!"EN_ATTENTE".equals(r.getStatut())) return r;
    r.setStatut("CONFIRMEE");
    return reservationRepository.save(r);
  }

  public Reservation annuler(String reservationId) {
    Reservation r = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Réservation introuvable"));
    if ("ANNULEE".equals(r.getStatut())) return r;
    r.setStatut("ANNULEE");
    return reservationRepository.save(r);
  }
}
