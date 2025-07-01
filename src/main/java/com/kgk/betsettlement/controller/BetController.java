package com.kgk.betsettlement.controller;

import com.kgk.betsettlement.dto.BetRequest;
import com.kgk.betsettlement.model.Bet;
import com.kgk.betsettlement.service.BetService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bets")
@RequiredArgsConstructor
@Slf4j
public class BetController {

  private final BetService betService;

  @PostMapping
  public ResponseEntity<Bet> createBet(@Valid @RequestBody BetRequest betRequest) {
    log.info("Received request to create bet: {}", betRequest);

    Bet savedBet = betService.createBet(betRequest);

    return ResponseEntity.ok(savedBet);
  }

  @GetMapping
  public ResponseEntity<List<Bet>> getAllBets() {
    log.info("Received request to get all bets");

    List<Bet> bets = betService.getAllBets();

    log.info("Returning {} bets", bets.size());
    return ResponseEntity.ok(bets);
  }

  @GetMapping("/event/{eventId}")
  public ResponseEntity<List<Bet>> getBetsByEventId(@PathVariable String eventId) {
    log.info("Received request to get bets for event: {}", eventId);

    List<Bet> bets = betService.getBetsByEventId(eventId);

    log.info("Returning {} bets for event: {}", bets.size(), eventId);
    return ResponseEntity.ok(bets);
  }
}