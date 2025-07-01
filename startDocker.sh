#!/bin/bash

echo "🧹 Usuwam stare pliki RocketMQ store..."
rm -rf ./data/rocketmq/store/*

echo "📂 Sprawdzam katalog na dane RocketMQ..."
mkdir -p ./data/rocketmq/store

echo "🔑 Ustawiam prawa zapisu do katalogu..."
chmod -R 777 ./data/rocketmq/store

echo "▶️ Uruchamianie pełnego stacku (infrastruktura + aplikacja)..."
docker compose --profile docker up -d --build

echo "⏳ Czekam 10 sekund, żeby wszystko wstało..."
sleep 10

echo "📝 Tworzę temat 'bet-settlements' w RocketMQ..."
docker exec rocketmq-broker sh mqadmin updateTopic \
  -n rocketmq-namesrv:9876 \
  -t bet-settlements \
  -c DefaultCluster \
  -r 1 \
  -w 1 || echo "ℹ️ Temat RocketMQ już istnieje – pomijam."

echo ""
echo "✅ Pełny stack gotowy!"
echo ""
echo "🌐 Dostępne usługi:"
echo "   • Aplikacja: http://localhost:9090"
echo "   • RocketMQ Console: http://localhost:9190"
echo "   • H2 Console: http://localhost:9090/h2-console"
echo ""