package dev.muriloabranches.avaliacao.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CreateVoteRequest(
        @NotBlank(message = "Vote value is mandatory.")
        String value,
        @NotNull(message = "User ID is mandatory.")
        Long userId
) {
}
