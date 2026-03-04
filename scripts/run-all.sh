#!/usr/bin/env bash
set -e

echo "Démarrage des microservices..."
(cd authentification-service && mvn -q spring-boot:run) & AUTH_PID=$!
(cd reservation-service && mvn -q spring-boot:run) & RES_PID=$!
(cd notification-service && mvn -q spring-boot:run) & NOTIF_PID=$!
(cd paiement-service && mvn -q spring-boot:run) & PAY_PID=$!

echo "PIDs : auth=$AUTH_PID reservation=$RES_PID notification=$NOTIF_PID paiement=$PAY_PID"
echo "Utilisez scripts/stop-all.sh pour arrêter."
wait
