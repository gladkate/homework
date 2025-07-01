package com.kgk.betsettlement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bet {

  @Id
  @NotBlank(message = "Bet ID cannot be blank")
  private String betId;

  @NotBlank(message = "User ID cannot be blank")
  private String userId;

  @NotBlank(message = "Event ID cannot be blank")
  private String eventId;

  @NotBlank(message = "Event winner ID cannot be blank")
  private String eventWinnerId;

  @NotNull(message = "Bet amount cannot be null")
  @Positive(message = "Bet amount must be positive")
  private BigDecimal betAmount;
}