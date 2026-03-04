package tg.togo.paiement.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientsHttp {

  @Bean
  WebClient clientReservation(@Value("${services.reservationBaseUrl}") String baseUrl) {
    return WebClient.builder().baseUrl(baseUrl).build();
  }

  @Bean
  WebClient clientNotification(@Value("${services.notificationBaseUrl}") String baseUrl) {
    return WebClient.builder().baseUrl(baseUrl).build();
  }
}
