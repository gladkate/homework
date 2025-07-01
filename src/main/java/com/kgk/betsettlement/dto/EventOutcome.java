package com.kgk.betsettlement.dto;

import jakarta.validation.constraints.NotBlank;

public record EventOutcome(
    @NotBlank(message = "Event ID cannot be blank")
    String eventId,

    @NotBlank(message = "Event name cannot be blank")
    String eventName,

    @NotBlank(message = "Event winner ID cannot be blank")
    String eventWinnerId
) {

}