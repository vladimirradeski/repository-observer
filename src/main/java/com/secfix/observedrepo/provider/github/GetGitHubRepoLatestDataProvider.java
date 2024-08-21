package com.secfix.observedrepo.provider.github;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.exception.ObservedRepoExternalDataNotFoundException;
import com.secfix.observedrepo.domain.gateway.GetObservedRepoLatestDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class GetGitHubRepoLatestDataProvider implements GetObservedRepoLatestDataProvider {

    private static final Logger log = LoggerFactory.getLogger(GetGitHubRepoLatestDataProvider.class);

    @Value("${secfix.github.api.url}")
    private String GITHUB_API_URL;

    private final RestTemplate restTemplate;

    public GetGitHubRepoLatestDataProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @Retryable(retryFor = Exception.class, noRetryFor = {HttpClientErrorException.NotFound.class, ObservedRepoExternalDataNotFoundException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ObservedRepo get(ObservedRepo observedRepo) {
        String uri = GITHUB_API_URL + "/repos/" + observedRepo.owner() + "/" + observedRepo.name();

        try {
            GitHubRepositoryModel gitHubRepositoryModel = restTemplate.getForObject(uri, GitHubRepositoryModel.class);
            return gitHubRepositoryModel != null ? gitHubRepositoryModel.toDomain() : null;
        } catch (HttpClientErrorException.NotFound ex) {
            log.error("GitHub repository not found: {}", observedRepo);
            throw new ObservedRepoExternalDataNotFoundException("GitHub repository not found");
        }
    }
}
