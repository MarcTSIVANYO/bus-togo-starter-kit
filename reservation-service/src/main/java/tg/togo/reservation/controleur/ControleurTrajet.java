package tg.togo.reservation.controleur;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tg.togo.reservation.modele.Trajet;
import tg.togo.reservation.repository.TrajetRepository;

import java.util.List;

@RestController
@RequestMapping("/api/trajets")
@RequiredArgsConstructor
public class ControleurTrajet {

  private final TrajetRepository repository;

  @PostMapping
  public Trajet creer(@RequestBody Trajet trajet) {
    return repository.save(trajet);
  }

  @GetMapping
  public List<Trajet> lister(
      @RequestParam(required = false) String villeDepart,
      @RequestParam(required = false) String villeArrivee
  ) {
    if (villeDepart != null && villeArrivee != null) {
      return repository.findByVilleDepartAndVilleArrivee(villeDepart, villeArrivee);
    }
    return repository.findAll();
  }
}
