package com.kgk.betsettlement.config;

import com.kgk.betsettlement.model.Bet;
import com.kgk.betsettlement.repository.BetRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

  private final BetRepository betRepository;

  @Override
  public void run(String... args) {
    log.info("Initializing sample bet data...");

    Bet bet1 = new Bet("bet001", "user1", "event123", "Lakers", new BigDecimal("100.00"));
    Bet bet2 = new Bet("bet002", "user2", "event123", "Warriors", new BigDecimal("50.00"));
    Bet bet3 = new Bet("bet003", "user3", "event123", "Lakers", new BigDecimal("75.00"));
    Bet bet4 = new Bet("bet004", "user1", "event456", "Bulls", new BigDecimal("200.00"));

    betRepository.save(bet1);
    betRepository.save(bet2);
    betRepository.save(bet3);
    betRepository.save(bet4);

    log.info("Initialized {} sample bets", betRepository.count());

    betRepository.findAll().forEach(bet ->
        log.info("Bet: {} - User: {} - Event: {} - Predicted Winner: {} - Amount: {}",
            bet.getBetId(), bet.getUserId(), bet.getEventId(),
            bet.getEventWinnerId(), bet.getBetAmount())
    );
  }
}