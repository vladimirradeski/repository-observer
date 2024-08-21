package com.secfix.observedrepo.provider.github.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    private static final String ACCEPT_HEADER_VALUE = "application/vnd.github+json";
    private static final String GITHUB_API_VERSION_HEADER_KEY = "X-GitHub-Api-Version";
    private static final String GITHUB_API_VERSION_HEADER_VALUE = "2022-11-28";
    @Value("${secfix.github.auth.token}")
    private String authToken;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.add(HttpHeaders.ACCEPT, ACCEPT_HEADER_VALUE);
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + authToken);
            headers.add(GITHUB_API_VERSION_HEADER_KEY, GITHUB_API_VERSION_HEADER_VALUE);
            return execution.execute(request, body);
        }));

        return restTemplate;
    }
}
