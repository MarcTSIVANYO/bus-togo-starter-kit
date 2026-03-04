package tg.togo.reservation.modele;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "uk_trajet_siege", columnNames = {"trajetId", "numeroSiege"}),
    @UniqueConstraint(name = "uk_idempotence", columnNames = {"cleIdempotence"})
})
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  // subject du JWT (email) : pour S4, on utilise email comme identifiant
  private String utilisateurId;

  private String trajetId;

  private int numeroSiege;

  // EN_ATTENTE / CONFIRMEE / ANNULEE
  private String statut;

  // header Idempotency-Key
  private String cleIdempotence;
}
