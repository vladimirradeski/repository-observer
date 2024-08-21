package com.secfix.observedrepo.domain.event.publisher;

import com.secfix.observedrepo.domain.event.model.ObservedRepoSyncedEvent;

public interface ObservedRepoSyncedEventPublisher {

    void publish(ObservedRepoSyncedEvent event);
}
