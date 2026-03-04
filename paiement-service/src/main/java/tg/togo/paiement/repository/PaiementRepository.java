package tg.togo.paiement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.togo.paiement.modele.Paiement;

public interface PaiementRepository extends JpaRepository<Paiement, String> {}
