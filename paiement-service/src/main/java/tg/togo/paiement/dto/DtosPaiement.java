package tg.togo.paiement.dto;

import lombok.Data;

public class DtosPaiement {

  @Data
  public static class DemandePaiement {
    private String reservationId;
    private int montant;
    private String methode; // MOMO, CARTE
  }

  @Data
  public static class ReponsePaiement {
    private String statutPaiement;     // SUCCES / ECHEC
    private String statutReservation;  // CONFIRMEE / ANNULEE
    private String message;
  }
}
