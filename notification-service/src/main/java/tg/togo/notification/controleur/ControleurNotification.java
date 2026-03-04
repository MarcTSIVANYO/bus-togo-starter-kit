package tg.togo.notification.controleur;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tg.togo.notification.service.ServiceNotification;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class ControleurNotification {

  private final ServiceNotification service;

  @PostMapping("/envoyer")
  public void envoyer(@RequestBody DemandeNotification req) {
    service.envoyer(req.reservationId, req.message, req.canal);
  }

  @Data
  public static class DemandeNotification {
    public String reservationId;
    public String message;
    public String canal; // SMS, EMAIL
  }
}
