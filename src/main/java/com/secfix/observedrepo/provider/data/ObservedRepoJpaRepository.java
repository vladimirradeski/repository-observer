package com.secfix.observedrepo.provider.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ObservedRepoJpaRepository extends JpaRepository<ObservedRepoModel, UUID> {

    Optional<ObservedRepoModel> findFirstByOwnerAndName(String owner, String name);

    @Query("SELECT e FROM ObservedRepoModel e WHERE " +
            "(:owner is null or e.owner = :owner) and " +
            "(:name is null or e.name = :name) and " +
            "(:status is null or e.status = :status) and " +
            "(:license is null or e.license = :license) and " +
            ":cursor > e.createdAt " +
            "order by e.createdAt DESC, e.id DESC"
    )
    List<ObservedRepoModel> findNextByCursor(
            @Param("owner") String owner,
            @Param("name") String name,
            @Param("status") ObservedRepoStatusModel status,
            @Param("license") String license,
            @Param("cursor") Instant cursor,
            Pageable pageable);

    @Query("SELECT e FROM ObservedRepoModel e WHERE " +
            "(:owner is null or e.owner = :owner) and " +
            "(:name is null or e.name = :name) and " +
            "(:status is null or e.status = :status) and " +
            "(:license is null or e.license = :license) and " +
            ":cursor < e.createdAt " +
            "order by e.createdAt DESC, e.id DESC"
    )
    List<ObservedRepoModel> findPrevByCursor(
            @Param("owner") String owner,
            @Param("name") String name,
            @Param("status") ObservedRepoStatusModel status,
            @Param("license") String license,
            @Param("cursor") Instant cursor,
            Pageable pageable);
}