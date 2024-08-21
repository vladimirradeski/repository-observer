package com.secfix.observedrepo.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateObservedRepoInput(
        @NotBlank(message = "The repo owner is required.")
        @Size(max = 1000, message = "The owner must have max 1000 characters.")
        String owner,
        @NotBlank(message = "The repo name is required.")
        @Size(max = 1000, message = "The name must have max 1000 characters.")
        String name
) {
}
