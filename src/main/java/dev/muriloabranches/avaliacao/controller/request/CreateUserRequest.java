package dev.muriloabranches.avaliacao.controller.request;

import javax.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank(message = "User name is mandatory.")
        String name
) {
}
