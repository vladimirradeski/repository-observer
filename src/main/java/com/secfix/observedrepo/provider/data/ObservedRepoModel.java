package com.secfix.observedrepo.provider.data;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.time.Instant;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "observed_repos")
public class ObservedRepoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @URL
    private String url;

    @Length(max = 1000)
    private String owner;

    @Length(max = 1000)
    private String name;

    private Integer stars;

    private Integer openIssues;

    @Length(max = 100)
    private String license;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant updatedAt;

    @Enumerated(EnumType.STRING)
    private ObservedRepoStatusModel status;

    @Version
    private Long version;

    public ObservedRepo toDomain() {
        return ObservedRepo.builder()
                .id(this.id)
                .url(this.url)
                .owner(this.owner)
                .name(this.name)
                .stars(this.stars)
                .openIssues(this.openIssues)
                .license(this.license)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .status(this.status.toDomain())
                .version(this.version)
                .build();
    }

    public static ObservedRepoModel fromDomain(ObservedRepo domain) {
        return ObservedRepoModel.builder()
                .id(domain.id())
                .url(domain.url())
                .owner(domain.owner())
                .name(domain.name())
                .stars(domain.stars())
                .openIssues(domain.openIssues())
                .license(domain.license())
                .createdAt(domain.createdAt())
                .updatedAt(domain.updatedAt())
                .status(ObservedRepoStatusModel.fromDomain(domain.status()))
                .version(domain.version())
                .build();
    }
}
