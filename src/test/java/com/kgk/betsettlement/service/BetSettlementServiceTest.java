package com.kgk.betsettlement.service;

import com.kgk.betsettlement.dto.BetSettlement;
import com.kgk.betsettlement.dto.EventOutcome;
import com.kgk.betsettlement.model.Bet;
import com.kgk.betsettlement.producer.BetSettlementPublisher;
import com.kgk.betsettlement.repository.BetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BetSettlementService using Mockito.
 * This is a pure unit test that doesn't load Spring context.
 */

@ExtendWith(MockitoExtension.class)
class BetSettlementServiceTest {

  @Mock
  private BetRepository betRepository;

  @Mock
  private BetSettlementPublisher betSettlementPublisher;

  @InjectMocks
  private BetSettlementService betSettlementService;

  @Test
  void shouldSettleWinningBet() {
    // given
    Bet winningBet = createBet("bet001", "user1", "event123", "team-lakers", new BigDecimal("100.00"));
    EventOutcome eventOutcome = new EventOutcome("event123", "Lakers vs Warriors", "team-lakers");

    when(betRepository.findByEventId("event123")).thenReturn(List.of(winningBet));

    // when
    betSettlementService.settleBetsForEventOutcome(eventOutcome);

    // then
    ArgumentCaptor<BetSettlement> settlementCaptor = ArgumentCaptor.forClass(BetSettlement.class);
    verify(betSettlementPublisher).sendSettlement(settlementCaptor.capture());

    BetSettlement settlement = settlementCaptor.getValue();
    assertThat(settlement.betId()).isEqualTo("bet001");
    assertThat(settlement.userId()).isEqualTo("user1");
    assertThat(settlement.eventId()).isEqualTo("event123");
    assertThat(settlement.betAmount()).isEqualTo(new BigDecimal("100.00"));
    assertThat(settlement.settlementStatus()).isEqualTo("WON");
  }

  @Test
  void shouldSettleLosingBet() {
    // given
    Bet losingBet = createBet("bet002", "user2", "event123", "team-warriors", new BigDecimal("50.00"));
    EventOutcome eventOutcome = new EventOutcome("event123", "Lakers vs Warriors", "team-lakers");

    when(betRepository.findByEventId("event123")).thenReturn(List.of(losingBet));

    // when
    betSettlementService.settleBetsForEventOutcome(eventOutcome);

    // then
    ArgumentCaptor<BetSettlement> settlementCaptor = ArgumentCaptor.forClass(BetSettlement.class);
    verify(betSettlementPublisher).sendSettlement(settlementCaptor.capture());

    BetSettlement settlement = settlementCaptor.getValue();
    assertThat(settlement.betId()).isEqualTo("bet002");
    assertThat(settlement.settlementStatus()).isEqualTo("LOST");
  }

  @Test
  void shouldSettleMultipleBetsForSameEvent() {
    // given
    Bet winningBet1 = createBet("bet001", "user1", "event123", "team-lakers", new BigDecimal("100.00"));
    Bet winningBet2 = createBet("bet002", "user2", "event123", "team-lakers", new BigDecimal("75.00"));
    Bet losingBet = createBet("bet003", "user3", "event123", "team-warriors", new BigDecimal("50.00"));

    EventOutcome eventOutcome = new EventOutcome("event123", "Lakers vs Warriors", "team-lakers");
    when(betRepository.findByEventId("event123")).thenReturn(List.of(winningBet1, winningBet2, losingBet));

    // when
    betSettlementService.settleBetsForEventOutcome(eventOutcome);

    // then
    verify(betSettlementPublisher, times(3)).sendSettlement(any(BetSettlement.class));

    ArgumentCaptor<BetSettlement> settlementCaptor = ArgumentCaptor.forClass(BetSettlement.class);
    verify(betSettlementPublisher, times(3)).sendSettlement(settlementCaptor.capture());

    List<BetSettlement> settlements = settlementCaptor.getAllValues();

    // Check winning bets
    assertThat(settlements).hasSize(3);
    assertThat(settlements).extracting(BetSettlement::settlementStatus)
        .containsExactly("WON", "WON", "LOST");
  }

  @Test
  void shouldHandleEventWithNoBets() {
    // given
    EventOutcome eventOutcome = new EventOutcome("event999", "Empty Event", "team-nobody");
    when(betRepository.findByEventId("event999")).thenReturn(Collections.emptyList());

    // when
    betSettlementService.settleBetsForEventOutcome(eventOutcome);

    // then
    verify(betSettlementPublisher, never()).sendSettlement(any(BetSettlement.class));
  }

  @Test
  void shouldCallRepositoryWithCorrectEventId() {
    // given
    EventOutcome eventOutcome = new EventOutcome("specific-event-id", "Test Event", "winner");
    when(betRepository.findByEventId("specific-event-id")).thenReturn(Collections.emptyList());

    // when
    betSettlementService.settleBetsForEventOutcome(eventOutcome);

    // then
    verify(betRepository).findByEventId("specific-event-id");
  }

  /**
   * Helper method to create test bets
   */
  private Bet createBet(String betId, String userId, String eventId, String eventWinnerId, BigDecimal betAmount) {
    return new Bet(betId, userId, eventId, eventWinnerId, betAmount);
  }
}