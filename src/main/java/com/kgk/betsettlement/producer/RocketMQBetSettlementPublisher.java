package com.kgk.betsettlement.producer;

import com.kgk.betsettlement.dto.BetSettlement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("docker")
public class RocketMQBetSettlementPublisher implements BetSettlementPublisher {

  private static final String BET_SETTLEMENTS_TOPIC = "bet-settlements";

  private final RocketMQTemplate rocketMQTemplate;

  @Override
  public void sendSettlement(BetSettlement betSettlement) {
    log.info("🚀 Sending bet settlement to RocketMQ topic '{}': {}", BET_SETTLEMENTS_TOPIC,
        betSettlement);

    rocketMQTemplate.asyncSend(BET_SETTLEMENTS_TOPIC, betSettlement, new SendCallback() {
      @Override
      public void onSuccess(SendResult sendResult) {
        log.info("✅ Successfully sent bet settlement for bet {} to RocketMQ. MessageId: {}",
            betSettlement.betId(), sendResult.getMsgId());
      }

      @Override
      public void onException(Throwable e) {
        log.error("Failed to send bet settlement for bet {} to RocketMQ: {}",
            betSettlement.betId(), e.getMessage());
      }
    });
  }
}