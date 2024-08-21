package com.secfix.observedrepo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secfix.observedrepo.api.dto.UpdateObservedRepoInput;
import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;
import com.secfix.observedrepo.domain.entity.dto.UpdateObservedRepoRequest;
import com.secfix.observedrepo.domain.usecase.UpdateObservedRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateObservedRepoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UpdateObservedRepo updateObservedRepo;

    @InjectMocks
    private UpdateObservedRepoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldUpdateObservedRepo() throws Exception {
        UUID repoId = UUID.randomUUID();
        UpdateObservedRepoInput input = UpdateObservedRepoInput.builder()
                .name("Updated Repo")
                .owner("Updated Owner")
                .url("https://updated-repo.com")
                .stars(1000)
                .openIssues(20)
                .license("MIT")
                .status(ObservedRepoStatus.ACTIVE)
                .build();

        ObservedRepo observedRepo = ObservedRepo.builder()
                .id(repoId)
                .name(input.name())
                .owner(input.owner())
                .url(input.url())
                .stars(input.stars())
                .openIssues(input.openIssues())
                .license(input.license())
                .status(input.status())
                .build();

        when(updateObservedRepo.execute(any(UpdateObservedRepoRequest.class))).thenReturn(observedRepo);

        mockMvc.perform(patch("/api/v1/observed-repos/{id}", repoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(observedRepo.id().toString()))
                .andExpect(jsonPath("$.name").value(observedRepo.name()))
                .andExpect(jsonPath("$.owner").value(observedRepo.owner()))
                .andExpect(jsonPath("$.url").value(observedRepo.url()))
                .andExpect(jsonPath("$.stars").value(observedRepo.stars()))
                .andExpect(jsonPath("$.openIssues").value(observedRepo.openIssues()))
                .andExpect(jsonPath("$.license").value(observedRepo.license()))
                .andExpect(jsonPath("$.status").value(observedRepo.status().toString()));


        verify(updateObservedRepo).execute(buildUpdateRequest(repoId, input));
    }

    private UpdateObservedRepoRequest buildUpdateRequest(UUID id, UpdateObservedRepoInput input) {
        return UpdateObservedRepoRequest.builder()
                .id(id)
                .name(input.name())
                .owner(input.owner())
                .url(input.url())
                .stars(input.stars())
                .openIssues(input.openIssues())
                .license(input.license())
                .status(input.status())
                .build();
    }
}