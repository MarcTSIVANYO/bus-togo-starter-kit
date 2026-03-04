package tg.togo.paiement.service;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tg.togo.paiement.dto.DtosPaiement.ReponsePaiement;
import tg.togo.paiement.modele.Paiement;
import tg.togo.paiement.repository.PaiementRepository;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class ServicePaiement {

  private final WebClient clientReservation;
  private final WebClient clientNotification;
  private final PaiementRepository paiementRepository;

  private final Random random = new Random();

  public ReponsePaiement payer(String reservationId, int montant, String methode) {
    ReponsePaiement rep = new ReponsePaiement();

    boolean ok = simulerPaiementExterneAvecRetry();

    Paiement paiement = new Paiement();
    paiement.setReservationId(reservationId);
    paiement.setMontant(montant);
    paiement.setMethode(methode);

    if (ok) {
      paiement.setStatut("SUCCES");
      paiementRepository.save(paiement);

      // Saga : confirmer réservation
      confirmerReservation(reservationId);
      rep.setStatutPaiement("SUCCES");
      rep.setStatutReservation("CONFIRMEE");
      rep.setMessage("Paiement OK -> réservation confirmée");

      // Notification non bloquante
      notifierAsync(reservationId, "Paiement validé. Votre réservation est confirmée.");
    } else {
      paiement.setStatut("ECHEC");
      paiementRepository.save(paiement);

      // Compensation : annuler réservation
      annulerReservation(reservationId);
      rep.setStatutPaiement("ECHEC");
      rep.setStatutReservation("ANNULEE");
      rep.setMessage("Paiement échoué -> réservation annulée");
    }

    return rep;
  }

  /**
   * Simule un provider de paiement :
   * - 15% erreur temporaire (déclenche retry)
   * - sinon ~70% succès
   */
  @Retry(name = "paiementRetry")
  protected boolean simulerPaiementExterneAvecRetry() {
    int x = random.nextInt(100);
    if (x < 15) {
      throw new RuntimeException("Erreur temporaire provider paiement");
    }
    return x < 85; // ~70% succès sur les appels non-temporairement en échec
  }

  private void confirmerReservation(String reservationId) {
    clientReservation.post()
        .uri("/api/reservations/{id}/confirmer", reservationId)
        .retrieve()
        .bodyToMono(String.class)
        .block();
  }

  private void annulerReservation(String reservationId) {
    clientReservation.post()
        .uri("/api/reservations/{id}/annuler", reservationId)
        .retrieve()
        .bodyToMono(String.class)
        .block();
  }

  private void notifierAsync(String reservationId, String message) {
    new Thread(() -> {
      try {
        clientNotification.post()
            .uri("/api/notifications/envoyer")
            .bodyValue(new DemandeNotification(reservationId, message, "SMS"))
            .retrieve()
            .bodyToMono(Void.class)
            .block();
      } catch (Exception ignored) {
        // best effort : on ne casse pas le workflow si notif indispo
      }
    }).start();
  }

  public record DemandeNotification(String reservationId, String message, String canal) {}
}
