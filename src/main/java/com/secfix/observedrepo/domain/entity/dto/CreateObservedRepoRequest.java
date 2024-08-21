package com.secfix.observedrepo.domain.entity.dto;

import lombok.Builder;

@Builder
public record CreateObservedRepoRequest(
        String owner,
        String name
) {
}
