package com.secfix.observedrepo.provider.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    private static final String OBSERVED_REPO_TOPIC_NAME = "observed-repo";

    @Bean
    public NewTopic observedRepoTopic() {
        return TopicBuilder.name(OBSERVED_REPO_TOPIC_NAME)
                .partitions(1)
                .replicas(1)
                .build();
    }
}

