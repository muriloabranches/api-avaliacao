package dev.muriloabranches.avaliacao.controller.request;

import javax.validation.constraints.NotBlank;

public record CreateAgendaRequest(
        @NotBlank(message = "Agenda name is mandatory.")
        String name,
        @NotBlank(message = "Agenda description is mandatory.")
        String description
) {
}
