# Starter Kit — Réservation de Bus (Togo) — Microservices Spring Boot (S4)

Ce dépôt contient un **starter kit prêt à cloner** pour un TP/Projet de Programmation Répartie.

## Microservices
- **authentification-service** (port **8081**) : inscription/connexion + JWT
- **reservation-service** (port **8082**) : trajets + réservations (409 conflit siège) + idempotence
- **paiement-service** (port **8083**) : paiement simulé + retry contrôlé + saga simplifiée (confirmer/annuler)
- **notification-service** (port **8084**) : notification (SMS/email mock) — asynchrone simulée

> Choix pédagogiques S4 :
> - Communication **REST HTTP**
> - Base **H2** par service (indépendance)
> - JWT simple (subject=email)
> - Résilience : **Retry** (paiement)
> - Asynchrone simulée : thread (à remplacer par RabbitMQ/Kafka en extension)

---

## Prérequis
- Java 17
- Maven 3.9+
- Postman (ou curl)

---

## Lancer en local (sans Docker)
Dans 4 terminaux :

```bash
cd authentification-service && mvn spring-boot:run
cd reservation-service && mvn spring-boot:run
cd paiement-service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run
```

---

## Lancer avec Docker Compose (optionnel)
```bash
docker compose up
```

---

## Test rapide (smoke-test)
```bash
bash scripts/smoke-test.sh
```

---

## API (contrat minimal)
### Authentification
- `POST /api/auth/inscription`
- `POST /api/auth/connexion`

### Réservation
- `POST /api/trajets`
- `GET /api/trajets`
- `POST /api/reservations`  
  Headers : `Authorization: Bearer <JWT>` et `Idempotency-Key: <clé>`
- `POST /api/reservations/{id}/confirmer` (interne saga)
- `POST /api/reservations/{id}/annuler` (interne saga)

### Paiement
- `POST /api/paiements/payer`

### Notification
- `POST /api/notifications/envoyer`

---

## Dataset suggéré (trajets au Togo)
- Lomé → Kara (40 sièges, 6000 FCFA)
- Lomé → Sokodé (35 sièges, 4500 FCFA)
- Lomé → Atakpamé (30 sièges, 2500 FCFA)
- Kara → Dapaong (28 sièges, 5000 FCFA)
- Atakpamé → Kpalimé (20 sièges, 1500 FCFA)

---

## Notes pédagogiques
- **409 Conflict** : siège déjà réservé sur un même trajet
- **Idempotence** : rejouer le même POST réservation avec la même clé ne crée pas un doublon
- **Saga simplifiée** : paiement => confirmer ; échec => annuler (compensation)
- Notification non bloquante (best effort)

Bon TP !
