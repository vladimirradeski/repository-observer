package com.secfix.observedrepo.api;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;
import com.secfix.observedrepo.domain.entity.dto.FindObservedReposRequest;
import com.secfix.observedrepo.domain.entity.dto.FindObservedReposResponse;
import com.secfix.observedrepo.domain.usecase.FindObservedRepos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FindObservedReposControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FindObservedRepos findObservedRepos;

    @InjectMocks
    private FindObservedReposController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldReturnFoundRepos() throws Exception {
        FindObservedReposRequest request = FindObservedReposRequest.builder()
                .owner("test-owner")
                .name("test-repo")
                .status(ObservedRepoStatus.ACTIVE)
                .license("MIT")
                .cursor("abc123")
                .pageSize(10)
                .next(true)
                .build();

        ObservedRepo observedRepo1 = ObservedRepo.builder()
                .id(UUID.randomUUID())
                .status(ObservedRepoStatus.ACTIVE)
                .owner("owner1")
                .name("name1")
                .build();

        ObservedRepo observedRepo2 = ObservedRepo.builder()
                .id(UUID.randomUUID())
                .status(ObservedRepoStatus.ACTIVE)
                .owner("owner2")
                .name("name2")
                .build();

        List<ObservedRepo> observedRepos = Arrays.asList(
                observedRepo1,
                observedRepo2
        );

        FindObservedReposResponse response = FindObservedReposResponse.builder()
                .nextCursor("next")
                .previousCursor("previous")
                .observedRepos(observedRepos)
                .build();

        when(findObservedRepos.execute(any(FindObservedReposRequest.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/observed-repos")
                        .param("owner", "test-owner")
                        .param("name", "test-repo")
                        .param("status", "ACTIVE")
                        .param("license", "MIT")
                        .param("cursor", "abc123")
                        .param("pageSize", "10")
                        .param("next", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.next").value("next"))
                .andExpect(jsonPath("$.previous").value("previous"))
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results.length()").value(observedRepos.size()))
                .andExpect(jsonPath("$.results[0].id").exists())
                .andExpect(jsonPath("$.results[0].name").value(observedRepo1.name()))
                .andExpect(jsonPath("$.results[0].owner").value(observedRepo1.owner()))
                .andExpect(jsonPath("$.results[1].id").exists())
                .andExpect(jsonPath("$.results[1].name").value(observedRepo2.name()))
                .andExpect(jsonPath("$.results[1].owner").value(observedRepo2.owner()));

        verify(findObservedRepos).execute(request);
    }
}