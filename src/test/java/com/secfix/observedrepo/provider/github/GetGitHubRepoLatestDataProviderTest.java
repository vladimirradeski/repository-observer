package com.secfix.observedrepo.provider.github;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;
import com.secfix.observedrepo.domain.entity.exception.ObservedRepoExternalDataNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetGitHubRepoLatestDataProviderTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GetGitHubRepoLatestDataProvider dataProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataProvider = new GetGitHubRepoLatestDataProvider(restTemplate);
    }

    @Test
    void testGetExistingRepo() {
        ObservedRepo observedRepo = ObservedRepo.builder()
                .id(UUID.randomUUID())
                .status(ObservedRepoStatus.ACTIVE)
                .owner("owner")
                .name("repo")
                .license("mit")
                .build();
        GitHubRepositoryModel gitHubRepositoryModel = new GitHubRepositoryModel();
        gitHubRepositoryModel.setName("repo");
        GitHubRepositoryModel.Owner owner = new GitHubRepositoryModel.Owner();
        owner.setLogin("owner");
        gitHubRepositoryModel.setOwner(owner);
        GitHubRepositoryModel.License license = new GitHubRepositoryModel.License();
        license.setKey("mit");
        gitHubRepositoryModel.setLicense(license);

        when(restTemplate.getForObject(anyString(), eq(GitHubRepositoryModel.class))).thenReturn(gitHubRepositoryModel);

        ObservedRepo result = dataProvider.get(observedRepo);

        assertNotNull(result);
        assertEquals("owner", result.owner());
        assertEquals("repo", result.name());
    }

    @Test
    void testGetRepoNotFound() {
        ObservedRepo observedRepo = ObservedRepo.builder()
                .id(UUID.randomUUID())
                .status(ObservedRepoStatus.ACTIVE)
                .owner("owner")
                .name("repo")
                .build();
        String uri = "https://api.github.com/repos/owner/repo";
        when(restTemplate.getForObject(anyString(), eq(GitHubRepositoryModel.class))).thenThrow(HttpClientErrorException.NotFound.class);

        assertThrows(ObservedRepoExternalDataNotFoundException.class, () -> {
            dataProvider.get(observedRepo);
        });
    }
}