package com.secfix.observedrepo.domain.gateway;

import com.secfix.observedrepo.domain.entity.ObservedRepo;

public interface GetObservedRepoLatestDataProvider {

    ObservedRepo get(ObservedRepo observedRepo);
}
