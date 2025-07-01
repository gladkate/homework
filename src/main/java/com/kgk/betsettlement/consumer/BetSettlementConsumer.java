package com.kgk.betsettlement.consumer;

import com.kgk.betsettlement.dto.EventOutcome;
import com.kgk.betsettlement.service.BetSettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BetSettlementConsumer {

  private final BetSettlementService betSettlementService;

  @KafkaListener(
      topics = "event-outcomes",
      groupId = "bet-settlement-group"
  )
  public void handleEventOutcome(EventOutcome eventOutcome) {
    log.info("Received event outcome: {}", eventOutcome);
    betSettlementService.settleBetsForEventOutcome(eventOutcome);
  }
}
