package com.secfix.observedrepo.domain.entity.dto;

import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UpdateObservedRepoRequest(

        UUID id,
        String owner,
        String name,
        String url,
        Integer stars,
        Integer openIssues,
        String license,
        ObservedRepoStatus status
) {
}
