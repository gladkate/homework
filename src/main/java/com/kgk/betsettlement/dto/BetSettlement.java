package com.kgk.betsettlement.dto;

import com.kgk.betsettlement.model.Bet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record BetSettlement(
    @NotBlank(message = "Bet ID cannot be blank")
    String betId,

    @NotBlank(message = "User ID cannot be blank")
    String userId,

    @NotBlank(message = "Event ID cannot be blank")
    String eventId,

    @NotNull(message = "Bet amount cannot be null")
    @Positive(message = "Bet amount must be positive")
    BigDecimal betAmount,

    @NotBlank(message = "Settlement status cannot be blank")
    String settlementStatus
) {

  public static BetSettlement from(Bet bet, String status) {
    return new BetSettlement(
        bet.getBetId(),
        bet.getUserId(),
        bet.getEventId(),
        bet.getBetAmount(),
        status
    );
  }
}