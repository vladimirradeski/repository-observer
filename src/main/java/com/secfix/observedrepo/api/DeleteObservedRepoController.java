package com.secfix.observedrepo.api;

import com.secfix.observedrepo.api.dto.ErrorResponse;
import com.secfix.observedrepo.api.dto.ObservedRepoResponse;
import com.secfix.observedrepo.domain.usecase.DeleteObservedRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Delete Observed Repo")
@RestController
@RequestMapping("/api/v1/observed-repos")
public class DeleteObservedRepoController {

    private static final Logger log = LoggerFactory.getLogger(DeleteObservedRepoController.class);

    private final DeleteObservedRepo deleteObservedRepo;

    public DeleteObservedRepoController(DeleteObservedRepo deleteObservedRepo) {
        this.deleteObservedRepo = deleteObservedRepo;
    }

    @Operation(
            summary = "API endpoint for deleting observed repo",
            description = "Perform soft delete for a given observed repo id. The Status will be changed to DELETED"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ObservedRepoResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())})})
    @DeleteMapping("/{id}")
    public ObservedRepoResponse deleteRepo(@Parameter(description = "Observed repo's id") @PathVariable UUID id) {
        log.info("DElETE_OBSERVED_REPO | id: {}", id);
        ObservedRepoResponse response = ObservedRepoResponse.fromDomain(deleteObservedRepo.execute(id));
        log.info("OBSERVED_REPO_DELETED | data: {}", response);
        return response;
    }
}
