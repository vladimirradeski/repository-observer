package com.secfix.observedrepo.api;

import com.secfix.observedrepo.api.dto.ErrorResponse;
import com.secfix.observedrepo.api.dto.ObservedRepoResponse;
import com.secfix.observedrepo.api.dto.UpdateObservedRepoInput;
import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.dto.UpdateObservedRepoRequest;
import com.secfix.observedrepo.domain.usecase.UpdateObservedRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Update Observed Repo")
@RestController
@RequestMapping("/api/v1/observed-repos")
public class UpdateObservedRepoController {

    private static final Logger log = LoggerFactory.getLogger(UpdateObservedRepoController.class);

    private final UpdateObservedRepo updateObservedRepo;

    public UpdateObservedRepoController(UpdateObservedRepo updateObservedRepo) {
        this.updateObservedRepo = updateObservedRepo;
    }

    @Operation(
            summary = "API endpoint for updating observed repo",
            description = "Update observed repo for a given id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ObservedRepoResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())})})
    @PatchMapping("/{id}")
    public ObservedRepoResponse updateRepo(@Parameter(description = "Observed repo's id") @PathVariable UUID id,
                                           @Valid @RequestBody UpdateObservedRepoInput input) {
        log.info("UPDATE_OBSERVED_REPO | id: {}, data: {}", id, input);
        UpdateObservedRepoRequest request = UpdateObservedRepoRequest.builder()
                .id(id)
                .name(input.name())
                .owner(input.owner())
                .url(input.url())
                .stars(input.stars())
                .openIssues(input.openIssues())
                .license(input.license())
                .status(input.status())
                .build();
        ObservedRepo observedRepo = updateObservedRepo.execute(request);
        ObservedRepoResponse response = ObservedRepoResponse.fromDomain(observedRepo);
        log.info("OBSERVED_REPO_UPDATED | data: {}", response);
        return response;
    }
}
