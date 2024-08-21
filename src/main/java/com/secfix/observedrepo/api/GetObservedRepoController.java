package com.secfix.observedrepo.api;

import com.secfix.observedrepo.api.dto.ErrorResponse;
import com.secfix.observedrepo.api.dto.ObservedRepoResponse;
import com.secfix.observedrepo.domain.usecase.GetObservedRepoById;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Get Observed Repo")
@RestController
@RequestMapping("/api/v1/observed-repos")
public class GetObservedRepoController {

    private final GetObservedRepoById getObservedRepoById;

    public GetObservedRepoController(GetObservedRepoById getObservedRepoById) {
        this.getObservedRepoById = getObservedRepoById;
    }

    @Operation(
            summary = "API endpoint for fetching observed repo",
            description = "Get observed repo for a given id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ObservedRepoResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())})})
    @GetMapping("/{id}")
    public ObservedRepoResponse getObservedRepo(
            @Parameter(description = "Observed repo's id") @PathVariable UUID id
    ) {
        return ObservedRepoResponse.fromDomain(getObservedRepoById.execute(id));
    }
}
