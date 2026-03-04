package tg.togo.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.togo.reservation.modele.Reservation;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
  Optional<Reservation> findByCleIdempotence(String cleIdempotence);
}
