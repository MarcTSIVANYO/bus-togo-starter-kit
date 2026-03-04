package tg.togo.paiement.controleur;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tg.togo.paiement.dto.DtosPaiement.DemandePaiement;
import tg.togo.paiement.dto.DtosPaiement.ReponsePaiement;
import tg.togo.paiement.service.ServicePaiement;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class ControleurPaiement {

  private final ServicePaiement service;

  @PostMapping("/payer")
  public ReponsePaiement payer(@RequestBody DemandePaiement req) {
    return service.payer(req.getReservationId(), req.getMontant(), req.getMethode());
  }
}
