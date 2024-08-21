package com.secfix.observedrepo.provider.kafka;

import com.secfix.observedrepo.domain.event.model.ObservedRepoSyncedEvent;
import com.secfix.observedrepo.domain.event.publisher.ObservedRepoSyncedEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaObservedRepoSyncedEventPublisher implements ObservedRepoSyncedEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaObservedRepoSyncedEventPublisher.class);

    private static final String TOPIC_NAME = "observed-repo";

    private static final String EVENT_KEY = "observed.repo.synced";

    private final KafkaTemplate<String, ObservedRepoSyncedEvent> kafkaTemplate;

    public KafkaObservedRepoSyncedEventPublisher(KafkaTemplate<String, ObservedRepoSyncedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private void send(ObservedRepoSyncedEvent value) {

        var future = kafkaTemplate.send(TOPIC_NAME, EVENT_KEY, value);

        future.whenComplete((sendResult, exception) -> {
            if (exception != null) {
                future.completeExceptionally(exception);
            } else {
                future.complete(sendResult);
            }
            log.info("Event sent to Kafka topic : " + value);
        });
    }

    @Override
    public void publish(ObservedRepoSyncedEvent event) {
        send(event);
    }
}
