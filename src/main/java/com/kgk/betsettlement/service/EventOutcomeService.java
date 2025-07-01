package com.kgk.betsettlement.service;

import com.kgk.betsettlement.dto.EventOutcome;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventOutcomeService {

  private static final String EVENT_OUTCOMES_TOPIC = "event-outcomes";

  private final KafkaTemplate<String, EventOutcome> kafkaTemplate;

  public void publishEventOutcome(EventOutcome eventOutcome) {
    log.info("Publishing event outcome to Kafka topic '{}': {}", EVENT_OUTCOMES_TOPIC,
        eventOutcome);

    kafkaTemplate.send(EVENT_OUTCOMES_TOPIC, eventOutcome.eventId(), eventOutcome);

    log.info("Successfully sent event outcome for event '{}' to topic '{}'",
        eventOutcome.eventId(), EVENT_OUTCOMES_TOPIC);
  }
}