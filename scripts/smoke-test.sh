#!/usr/bin/env bash
set -e

echo "1) Inscription (best effort si déjà inscrit)..."
curl -s -X POST http://localhost:8081/api/auth/inscription   -H "Content-Type: application/json"   -d '{"email":"etudiant1@univ.tg","motDePasse":"pass123","nomComplet":"Etudiant 1"}' >/dev/null || true

echo "2) Connexion..."
TOKEN=$(curl -s -X POST http://localhost:8081/api/auth/connexion   -H "Content-Type: application/json"   -d '{"email":"etudiant1@univ.tg","motDePasse":"pass123"}' | sed -n 's/.*"token":"\([^"]*\)".*/\1/p')

if [ -z "$TOKEN" ]; then
  echo "Impossible de récupérer le token. Vérifiez auth-service."
  exit 1
fi
echo "Token OK (len=${#TOKEN})"

echo "3) Création d'un trajet..."
TRIP=$(curl -s -X POST http://localhost:8082/api/trajets   -H "Content-Type: application/json"   -d '{"villeDepart":"Lome","villeArrivee":"Sokode","dateDepart":"2026-03-10 07:00","nombreSiegesTotal":35,"prixFcfa":4500}')
TRIP_ID=$(echo "$TRIP" | sed -n 's/.*"id":"\([^"]*\)".*/\1/p')
echo "Trajet id=$TRIP_ID"

echo "4) Création d'une réservation (PENDING)..."
RES=$(curl -s -X POST http://localhost:8082/api/reservations   -H "Content-Type: application/json"   -H "Authorization: Bearer $TOKEN"   -H "Idempotency-Key: demo-abc-123"   -d "{"trajetId":"$TRIP_ID","numeroSiege":12}")
RES_ID=$(echo "$RES" | sed -n 's/.*"id":"\([^"]*\)".*/\1/p')
echo "Reservation id=$RES_ID"

echo "5) Paiement (saga)..."
curl -s -X POST http://localhost:8083/api/paiements/payer   -H "Content-Type: application/json"   -d "{"reservationId":"$RES_ID","montant":4500,"methode":"MOMO"}"
echo ""
echo "=> Consultez les logs de notification-service si SUCCESS."
