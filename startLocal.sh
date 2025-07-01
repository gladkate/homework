#!/bin/bash

echo "▶️ Uruchamianie infrastruktury dla lokalnego developmentu..."
echo "ℹ️ Tylko Kafka + Zookeeper (RocketMQ pomijany - aplikacja używa local mode)"
docker compose up -d kafka

echo "⏳ Czekam 10 sekund, żeby Kafka wstał..."
sleep 10

echo ""
echo "✅ Infrastruktura gotowa!"
echo ""
echo "🎯 Teraz możesz uruchomić aplikację lokalnie:"
echo "   mvn spring-boot:run -Dspring-boot.run.profiles=local"
echo ""
echo "🌐 Dostępne usługi:"
echo "   • H2 Console (po uruchomieniu app): http://localhost:9090/h2-console"
echo ""
echo "📋 W trybie LOCAL:"
echo "   • Event outcomes: Kafka (normalnie)"
echo "   • Bet settlements: Tylko logi (LocalBetSettlementPublisher)"
echo ""