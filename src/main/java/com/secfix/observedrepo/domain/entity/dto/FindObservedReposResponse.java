package com.secfix.observedrepo.domain.entity.dto;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import lombok.Builder;

import java.util.List;

@Builder
public record FindObservedReposResponse(
        String nextCursor,
        String previousCursor,
        List<ObservedRepo> observedRepos
) {

}
