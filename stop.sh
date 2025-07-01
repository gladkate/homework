#!/bin/bash

echo "⛔ Zatrzymywanie wszystkich środowisk..."

echo "🔍 Sprawdzam uruchomione kontenery..."
RUNNING_CONTAINERS=$(docker compose ps --services --filter "status=running" 2>/dev/null || echo "")

if [ -z "$RUNNING_CONTAINERS" ]; then
    echo "ℹ️ Brak uruchomionych kontenerów w docker-compose."
else
    echo "📦 Znalezione kontenery: $RUNNING_CONTAINERS"

    echo "⛔ Zatrzymywanie infrastruktury..."
    docker compose down --remove-orphans

    echo "⛔ Zatrzymywanie aplikacji Docker (jeśli uruchomiona)..."
    docker compose --profile docker down --remove-orphans
fi

echo ""
echo "✅ Wszystkie kontenery zatrzymane."
echo ""
echo "💡 Jeśli uruchamiałeś aplikację lokalnie (Maven), zatrzymaj ją również:"
echo "   - Ctrl+C w terminalu z 'mvn spring-boot:run'"
echo "   - Lub zatrzymaj w IDE"
echo ""