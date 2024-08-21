package com.secfix.observedrepo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secfix.observedrepo.api.dto.CreateObservedRepoInput;
import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;
import com.secfix.observedrepo.domain.entity.dto.CreateObservedRepoRequest;
import com.secfix.observedrepo.domain.usecase.CreateObservedRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateObservedRepoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateObservedRepo createObservedRepo;

    @InjectMocks
    private CreateObservedRepoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testCreateRepo() throws Exception {
        CreateObservedRepoInput input = CreateObservedRepoInput.builder()
                .name("New Repo")
                .owner("New Owner")
                .build();

        CreateObservedRepoRequest expectedRequest = CreateObservedRepoRequest.builder()
                .name(input.name())
                .owner(input.owner())
                .build();

        ObservedRepo observedRepo = ObservedRepo.builder()
                .name(input.name())
                .owner(input.owner())
                .status(ObservedRepoStatus.ACTIVE)
                .build();

        when(createObservedRepo.execute(expectedRequest)).thenReturn(observedRepo);

        mockMvc.perform(post("/api/v1/observed-repos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(observedRepo.name()))
                .andExpect(jsonPath("$.owner").value(observedRepo.owner()));

        verify(createObservedRepo).execute(expectedRequest);
    }
}