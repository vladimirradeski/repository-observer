package com.secfix.observedrepo.api.dto;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ObservedRepoResponse(
        UUID id,
        String url,
        String owner,
        String name,
        Integer stars,
        Integer openIssues,
        String license,
        Instant createdAt,
        Instant updatedAt,
        String status
) {

    public static ObservedRepoResponse fromDomain(ObservedRepo domain) {
        return ObservedRepoResponse.builder()
                .id(domain.id())
                .url(domain.url())
                .owner(domain.owner())
                .name(domain.name())
                .stars(domain.stars())
                .openIssues(domain.openIssues())
                .license(domain.license())
                .createdAt(domain.createdAt())
                .updatedAt(domain.updatedAt())
                .status(domain.status().toString())
                .build();
    }
}
