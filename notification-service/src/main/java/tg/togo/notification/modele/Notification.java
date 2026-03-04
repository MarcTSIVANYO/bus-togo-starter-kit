package tg.togo.notification.modele;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String reservationId;

  private String message;

  private String canal; // SMS, EMAIL

  private String statut; // ENVOYEE, ECHEC
}
