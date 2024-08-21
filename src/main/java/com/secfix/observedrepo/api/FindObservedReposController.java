package com.secfix.observedrepo.api;

import com.secfix.observedrepo.api.dto.ErrorResponse;
import com.secfix.observedrepo.api.dto.FindObservedReposPagedResponse;
import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;
import com.secfix.observedrepo.domain.entity.dto.FindObservedReposRequest;
import com.secfix.observedrepo.domain.usecase.FindObservedRepos;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Get All Observed Repos")
@RestController
@RequestMapping("/api/v1/observed-repos")
public class FindObservedReposController {

    private final FindObservedRepos findObservedRepos;

    public FindObservedReposController(FindObservedRepos findObservedRepos) {
        this.findObservedRepos = findObservedRepos;
    }

    @Operation(
            summary = "API endpoint for fetching list of observed repos",
            description = "Fetch list of observed repos by specifying search params and pagination"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = FindObservedReposPagedResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())})})
    @GetMapping()
    public FindObservedReposPagedResponse findObservedRepos(
            @Parameter(description = "Filter observed repos by owner") @RequestParam(required = false) String owner,
            @Parameter(description = "Filter observed repos by name") @RequestParam(required = false) String name,
            @Parameter(description = "Filter observed repos by status") @RequestParam(required = false) String status,
            @Parameter(description = "Filter observed repos by license") @RequestParam(required = false) String license,
            @Parameter(description = "Pagination cursor for next or prev page") @RequestParam(required = false) String cursor,
            @Parameter(description = "Size of returned page") @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @Parameter(description = "Next or previous direction for pagination") @RequestParam(required = false, defaultValue = "true") boolean next
    ) {
        FindObservedReposRequest request = FindObservedReposRequest.builder()
                .owner(owner)
                .name(name)
                .status(status != null ? ObservedRepoStatus.valueOf(status) : null)
                .license(license)
                .cursor(cursor)
                .pageSize(pageSize)
                .next(next)
                .build();

        return FindObservedReposPagedResponse.fromDomain(findObservedRepos.execute(request));
    }
}
