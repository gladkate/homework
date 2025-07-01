package com.kgk.betsettlement.service;

import com.kgk.betsettlement.dto.BetSettlement;
import com.kgk.betsettlement.dto.EventOutcome;
import com.kgk.betsettlement.producer.BetSettlementPublisher;
import com.kgk.betsettlement.repository.BetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BetSettlementService {

  private final BetRepository betRepository;
  private final BetSettlementPublisher betSettlementPublisher;

  public void settleBetsForEventOutcome(EventOutcome eventOutcome) {
    log.info("Starting settlement for event: {}", eventOutcome);

    betRepository.findByEventId(eventOutcome.eventId()).forEach(bet -> {
      boolean betWon = bet.getEventWinnerId().equals(eventOutcome.eventWinnerId());
      String settlementStatus = betWon ? "WON" : "LOST";

      log.info("Bet {} for user {} - Status: {} (predicted: {}, actual: {})",
          bet.getBetId(),
          bet.getUserId(),
          settlementStatus,
          bet.getEventWinnerId(),
          eventOutcome.eventWinnerId());

      BetSettlement settlement = BetSettlement.from(bet, settlementStatus);
      betSettlementPublisher.sendSettlement(settlement);
    });

    log.info("Finished settlement for event: {}", eventOutcome.eventId());
  }
}
