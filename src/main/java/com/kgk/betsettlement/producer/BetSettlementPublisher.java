package com.kgk.betsettlement.producer;

import com.kgk.betsettlement.dto.BetSettlement;

/**
 * Interface for publishing bet settlements. Different implementations can be used based on active
 * Spring profiles.
 */
public interface BetSettlementPublisher {

  /**
   * Publishes a bet settlement.
   *
   * @param betSettlement the settlement to publish
   */
  void sendSettlement(BetSettlement betSettlement);
}