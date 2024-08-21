package com.secfix.observedrepo.api;

import com.secfix.observedrepo.api.dto.CreateObservedRepoInput;
import com.secfix.observedrepo.api.dto.ErrorResponse;
import com.secfix.observedrepo.api.dto.ObservedRepoResponse;
import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.dto.CreateObservedRepoRequest;
import com.secfix.observedrepo.domain.usecase.CreateObservedRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Create Observed Repo")
@RestController
@RequestMapping("/api/v1/observed-repos")
public class CreateObservedRepoController {

    private static final Logger log = LoggerFactory.getLogger(CreateObservedRepoController.class);

    private final CreateObservedRepo createObservedRepo;

    public CreateObservedRepoController(CreateObservedRepo createObservedRepo) {
        this.createObservedRepo = createObservedRepo;
    }

    @Operation(
            summary = "API endpoint for creating new observed repo",
            description = "Create new observed repo by specifying its owner and name"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ObservedRepoResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())})})
    @PostMapping()
    public ObservedRepoResponse createRepo(@Valid @RequestBody CreateObservedRepoInput input) {
        log.info("CREATE_OBSERVED_REPO | data: {}", input);
        CreateObservedRepoRequest request = CreateObservedRepoRequest.builder()
                .name(input.name())
                .owner(input.owner())
                .build();
        ObservedRepo observedRepo = createObservedRepo.execute(request);
        ObservedRepoResponse response = ObservedRepoResponse.fromDomain(observedRepo);
        log.info("OBSERVED_REPO_CREATED | data: {}", response);
        return response;
    }
}
