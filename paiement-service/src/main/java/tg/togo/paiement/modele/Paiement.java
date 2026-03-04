package tg.togo.paiement.modele;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Paiement {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String reservationId;

  private int montant;

  private String methode; // MOMO, CARTE...

  private String statut;  // SUCCES, ECHEC
}
