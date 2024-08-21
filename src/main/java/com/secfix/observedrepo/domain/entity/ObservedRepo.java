package com.secfix.observedrepo.domain.entity;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ObservedRepo(
        UUID id,
        String url,
        String owner,
        String name,
        Integer stars,
        Integer openIssues,
        String license,
        Instant createdAt,
        Instant updatedAt,
        ObservedRepoStatus status,
        Long version
) {
    public ObservedRepo invalidate() {
        return new ObservedRepo(id, url, owner, name, stars, openIssues, license, createdAt, updatedAt, ObservedRepoStatus.INVALID, version);
    }

    public ObservedRepo delete() {
        return new ObservedRepo(id, url, owner, name, stars, openIssues, license, createdAt, updatedAt, ObservedRepoStatus.DELETED, version);
    }
}
