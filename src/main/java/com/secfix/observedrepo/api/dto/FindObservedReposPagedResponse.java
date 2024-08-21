package com.secfix.observedrepo.api.dto;

import com.secfix.observedrepo.domain.entity.dto.FindObservedReposResponse;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record FindObservedReposPagedResponse(
        String next,
        String previous,
        List<ObservedRepoResponse> results
) {
    public static FindObservedReposPagedResponse fromDomain(FindObservedReposResponse domain) {
        return FindObservedReposPagedResponse.builder()
                .next(domain.nextCursor())
                .previous(domain.previousCursor())
                .results(domain.observedRepos().stream()
                        .map(ObservedRepoResponse::fromDomain)
                        .collect(Collectors.toList()))
                .build();
    }
}
