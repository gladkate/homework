package com.kgk.betsettlement.controller;

import com.kgk.betsettlement.dto.EventOutcome;
import com.kgk.betsettlement.service.EventOutcomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
public class EventOutcomeController {

  private final EventOutcomeService eventOutcomeService;

  @PostMapping("/outcomes")
  public ResponseEntity<String> publishEventOutcome(@Valid @RequestBody EventOutcome eventOutcome) {
    log.info("Received event outcome: {}", eventOutcome);

    eventOutcomeService.publishEventOutcome(eventOutcome);

    log.info("Successfully processed event outcome for event: {}", eventOutcome.eventId());
    return ResponseEntity.ok("Event outcome published successfully");
  }
}