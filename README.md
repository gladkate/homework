# 🎯 Bet Settlement Service – Recruitment Task

A backend application in Java that:
- Accepts a sports event outcome
- Settles bets stored in an in-memory database (H2)
- Publishes settlement messages to RocketMQ (docker profile) or logs info in the console (local profile)

---

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 3.5.3**
- **Apache Kafka** (via Docker)
- **Apache RocketMQ** (via Docker)
- **Spring Data JPA + H2** (in-memory)
- **Lombok**
- **Docker Compose** for local setup

---

## ⚙️ Prerequisites

- **[Docker Desktop](https://www.docker.com/products/docker-desktop)** installed and running
- **Java 21** (for local development)
- **Maven 3.9+** (for local development, or use included `./mvnw` wrapper)

Verify Docker is running:
```bash
docker compose version
```

> ✅ **Note for Apple Silicon (M1/M2/M3)**  
> RocketMQ official Docker images are only available for `linux/amd64`.  
> The `docker-compose.yml` enforces `platform: linux/amd64` for RocketMQ services.  
> Docker Desktop handles this with QEMU emulation automatically.

---

## 🚀 Quick Start

### Option 1: Local Development (Recommended for development)

**Run infrastructure in Docker + App locally on your machine**

1. **Make scripts executable (first time only):**
   ```bash
   chmod +x startLocal.sh startDocker.sh
   ```

2. **Start infrastructure (Kafka + RocketMQ) in Docker:**
   ```bash
   ./startLocal.sh
   ```

3. **Run application locally with Maven:**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=local
   ```

   *Alternative: Run from your IDE (IntelliJ, VS Code, etc.) with `local` profile*

### Option 2: Full Docker Stack

**Run everything (infrastructure + app) in Docker containers**

```bash
./startDocker.sh
```
---

## 🎯 What Each Script Does

### `./startLocal.sh`
- Starts only infrastructure: Kafka, Zookeeper
- **Does NOT start RocketMQ** - not needed in local mode
- **Does NOT start the application** - you run it locally

### `./startDocker.sh`
- Cleans old RocketMQ data
- Builds and starts everything including the application
- Creates necessary topics
- **Application runs inside Docker container**

### Maven Command Explained
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
- `mvn spring-boot:run` - Maven command to run Spring Boot app
- `-Dspring-boot.run.profiles=local` - Use "local" configuration (connects to localhost:9092 (Kafka))

### Alternative: Run from IDE
Instead of Maven, you can run from IntelliJ/VS Code:
1. Set VM options: `-Dspring.profiles.active=local`
2. Run the main class: `BetSettlementApplication`

---

## 📂 Configuration Profiles

### Local Profile (`application-local.properties`)
- **Port:** 9090
- **Kafka:** localhost:9092
- **Usage:** Development with IDE/Maven

### Docker Profile (`application-docker.properties`)
- **Port:** 9090
- **RocketMQ:** rocketmq-namesrv:9876
- **Kafka:** kafka:29092

---

### H2 Database Console Access
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **User:** `sa`
- **Password:** *(leave empty)*

---

## 📬 API Endpoints

### ➕ Create a Bet

`POST /api/v1/bets`

Example body:
```json
{
  "betId": "bet-001",
  "userId": "user-123",
  "eventId": "event-abc",
  "eventMarketId": "market-1",
  "eventWinnerId": "team-x",
  "betAmount": 100.00
}
```

### 📣 Publish an Event Outcome

`POST /api/v1/events/outcomes`

Example body:
```json
{
  "eventId": "event-abc",
  "eventName": "Lakers vs Warriors",
  "eventWinnerId": "team-x"
}
```

Example cURL:
```bash
curl -X POST http://localhost:9090/api/v1/events/outcomes \
-H "Content-Type: application/json" \
-d '{
  "eventId": "event-abc",
  "eventName": "Lakers vs Warriors",
  "eventWinnerId": "team-x"
}'
```
---

## 🔄 Data Flow

1. **Create Bets:** `POST /api/v1/bets` stores bets in H2 database
2. **Event Outcome:** `POST /api/v1/events/outcomes` publishes event outcome to Kafka topic `event-outcomes`
3. **Kafka Consumer:** Listens for event outcomes and processes bet settlements
4. **Settlement:** Matches outcomes with bets and publishes settlements:
   - **Local mode:** Logs settlement info to console
   - **Docker mode:** Sends to RocketMQ topic `bet-settlements`
5. **RocketMQ Consumer:** Processes settlement messages (logs them for debugging)

---

## 🧪 How to Test

### Quick Test Scenario

1. **Start the application** (using either option above)

2. **Create a bet:**
   ```bash
   curl -X POST http://localhost:9090/api/v1/bets \
   -H "Content-Type: application/json" \
   -d '{
     "betId": "bet-001",
     "userId": "user-123",
     "eventId": "event-abc",
     "eventMarketId": "market-1",
     "eventWinnerId": "team-x",
     "betAmount": 100.00
   }'
   ```

3. **Publish an event outcome:**
   ```bash
   curl -X POST http://localhost:9090/api/v1/events/outcomes \
   -H "Content-Type: application/json" \
   -d '{
     "eventId": "event-abc",
     "eventName": "Lakers vs Warriors",
     "eventWinnerId": "team-x"
   }'
   ```

4. **Check logs** - you should see:
- Event outcome received by Kafka consumer
- Bet matched and settled
- Settlement message sent to RocketMQ or logs in the console
- RocketMQ consumer processing the settlement

---

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

All tests use profile-based configuration and embedded Kafka to avoid external dependencies.

---

### Clean Up & Reset

**Stop everything:**
```bash
./stop.sh
```

**Remove all data (reset environment):**
```bash
./reset.sh
```
---

## 📊 Monitoring

### RocketMQ Console (docker profile only)
Visit **http://localhost:9190** to:
- Monitor message queues and topics
- View consumer groups and their status
- Check broker health and statistics
- Manage topics and subscriptions

---

### Useful Docker Commands
```bash
# View logs
docker compose logs -f betsettlement
docker compose logs -f rocketmq-broker

# Check container status
docker compose ps

# Rebuild application only
docker compose build betsettlement
docker compose up -d betsettlement
```