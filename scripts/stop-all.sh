#!/usr/bin/env bash
echo "Arrêt (best effort)..."
pkill -f "spring-boot:run" || true
pkill -f "org.springframework.boot.loader" || true
echo "OK."
