#!/bin/bash

echo "🧹 Resetowanie środowiska (z czyszczeniem danych kontenerów i wolumenów)..."

echo "🔍 Sprawdzam uruchomione kontenery..."
docker compose ps

echo "⛔ Zatrzymywanie wszystkich kontenerów..."
docker compose down --volumes --remove-orphans

echo "⛔ Zatrzymywanie profilu docker..."
docker compose --profile docker down --volumes --remove-orphans

echo "🧹 Zatrzymywanie wszystkich kontenerów projektu (force)..."
docker stop $(docker ps -a -q --filter "label=com.docker.compose.project=betsettlement" 2>/dev/null) 2>/dev/null || true
docker rm $(docker ps -a -q --filter "label=com.docker.compose.project=betsettlement" 2>/dev/null) 2>/dev/null || true

echo "🌐 Usuwam sieć (jeśli istnieje)..."
docker network rm betsettlement_default 2>/dev/null || true

echo "🗑️ Usuwanie lokalnych danych RocketMQ..."
rm -rf ./data/rocketmq/store

echo "🔄 Przywracanie struktury katalogów..."
mkdir -p ./data/rocketmq/store
chmod -R 777 ./data/rocketmq/store

echo ""
echo "✅ Reset wykonany."
echo ""
echo "🚀 Możesz teraz uruchomić świeże środowisko:"
echo "   ./startLocal.sh  (dla lokalnego developmentu)"
echo "   ./startDocker.sh (dla pełnego Docker stacku)"
echo ""