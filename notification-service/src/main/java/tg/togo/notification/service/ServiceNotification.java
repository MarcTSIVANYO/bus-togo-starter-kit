package tg.togo.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tg.togo.notification.modele.Notification;
import tg.togo.notification.repository.NotificationRepository;

@Service
@RequiredArgsConstructor
public class ServiceNotification {

  private final NotificationRepository repository;

  public void envoyer(String reservationId, String message, String canal) {
    Notification n = new Notification();
    n.setReservationId(reservationId);
    n.setMessage(message);
    n.setCanal(canal);
    n.setStatut("ENVOYEE");
    repository.save(n);

    // Simulation SMS/Email
    System.out.println("[NOTIFICATION] canal=" + canal + " reservationId=" + reservationId + " message=" + message);
  }
}
