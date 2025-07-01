package com.kgk.betsettlement.producer;

import com.kgk.betsettlement.dto.BetSettlement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Profile("local")
public class LocalBetSettlementPublisher implements BetSettlementPublisher {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd HH:mm:ss");

  @Override
  public void sendSettlement(BetSettlement betSettlement) {
    String timestamp = LocalDateTime.now().format(FORMATTER);

    log.info("📋 LOCAL MODE: Bet Settlement Published");
    log.info("┌─────────────────────────────────────────────────────────────────");
    log.info("│ 🎲 Bet ID: {}", betSettlement.betId());
    log.info("│ 👤 User ID: {}", betSettlement.userId());
    log.info("│ 🏆 Event ID: {}", betSettlement.eventId());
    log.info("│ 💰 Amount: ${}", betSettlement.betAmount());
    log.info("│ 📊 Status: {}", betSettlement.settlementStatus());
    log.info("│ ⏰ Timestamp: {}", timestamp);
    log.info("└─────────────────────────────────────────────────────────────────");

    // Simulate processing time for realism
    try {
      Thread.sleep(100);
      log.info("✅ LOCAL: Settlement for bet {} processed successfully", betSettlement.betId());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.warn("⚠️ LOCAL: Settlement processing interrupted for bet {}", betSettlement.betId());
    }
  }
}