package com.kgk.betsettlement.service;

import com.kgk.betsettlement.dto.BetRequest;
import com.kgk.betsettlement.model.Bet;
import com.kgk.betsettlement.repository.BetRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BetService {

  private final BetRepository betRepository;

  public Bet createBet(BetRequest betRequest) {
    log.debug("Creating bet: {}", betRequest);

    if (betRepository.existsById(betRequest.betId())) {
      throw new IllegalArgumentException("Bet with ID " + betRequest.betId() + " already exists");
    }

    Bet bet = convertToEntity(betRequest);
    Bet savedBet = betRepository.save(bet);

    log.info("Successfully created bet with ID: {}", savedBet.getBetId());
    return savedBet;
  }

  public List<Bet> getAllBets() {
    log.debug("Retrieving all bets");
    return betRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<Bet> getBetsByEventId(String eventId) {
    log.debug("Retrieving bets for event: {}", eventId);
    return betRepository.findByEventId(eventId);
  }

  private Bet convertToEntity(BetRequest betRequest) {
    return new Bet(
        betRequest.betId(),
        betRequest.userId(),
        betRequest.eventId(),
        betRequest.eventWinnerId(),
        betRequest.betAmount()
    );
  }
}