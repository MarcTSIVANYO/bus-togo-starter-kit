package tg.togo.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.togo.reservation.modele.Trajet;

import java.util.List;

public interface TrajetRepository extends JpaRepository<Trajet, String> {
  List<Trajet> findByVilleDepartAndVilleArrivee(String villeDepart, String villeArrivee);
}
