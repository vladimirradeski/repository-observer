package com.secfix.observedrepo.api;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;
import com.secfix.observedrepo.domain.usecase.DeleteObservedRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteObservedRepoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DeleteObservedRepo deleteObservedRepo;

    @Mock
    private Logger log;

    @InjectMocks
    private DeleteObservedRepoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldDeleteObservedRepo() throws Exception {
        UUID repoId = UUID.randomUUID();
        ObservedRepo observedRepo = ObservedRepo.builder()
                .id(repoId)
                .status(ObservedRepoStatus.DELETED)
                .build();

        when(deleteObservedRepo.execute(any(UUID.class))).thenReturn(observedRepo);

        mockMvc.perform(delete("/api/v1/observed-repos/{id}", repoId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(observedRepo.id().toString()))
                .andExpect(jsonPath("$.name").value(observedRepo.name()))
                .andExpect(jsonPath("$.status").value(observedRepo.status().toString()));

        verify(deleteObservedRepo).execute(repoId);
    }
}