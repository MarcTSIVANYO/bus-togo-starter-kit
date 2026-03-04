package tg.togo.reservation.modele;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Trajet {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String villeDepart;
  private String villeArrivee;

  // TP simplifié : String. Extension : LocalDateTime.
  private String dateDepart;

  private int nombreSiegesTotal;

  private int prixFcfa;
}
