package com.kgk.betsettlement;

import com.kgk.betsettlement.producer.BetSettlementPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration test to verify that Spring Boot application context loads correctly. Uses embedded
 * Kafka to avoid external dependencies.
 */
@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {"event-outcomes"})
class BetSettlementApplicationTests {

  @TestConfiguration
  static class TestConfig {

    @Bean
    BetSettlementPublisher betSettlementPublisher() {
      return org.mockito.Mockito.mock(BetSettlementPublisher.class);
    }
  }

  @Test
  void contextLoads() {
    // This test verifies that the Spring Boot application context
    // can be loaded successfully with all beans properly configured
  }
}