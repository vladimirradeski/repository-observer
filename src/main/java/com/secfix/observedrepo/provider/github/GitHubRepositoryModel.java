package com.secfix.observedrepo.provider.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.secfix.observedrepo.domain.entity.ObservedRepo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubRepositoryModel {

    public String name;
    public Owner owner;
    public String url;
    @JsonProperty("stargazers_count")
    public int stargazersCount;
    @JsonProperty("open_issues_count")
    public int openIssuesCount;
    @JsonProperty("created_at")
    public String createdAt;
    @JsonProperty("updated_at")
    public String updatedAt;
    public License license;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Setter
    @Getter
    public static class Owner {
        public String login;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Setter
    @Getter
    public static class License {
        public String key;
    }

    public ObservedRepo toDomain() {
        return ObservedRepo.builder()
                .url(this.url)
                .owner(this.owner.login)
                .name(this.name)
                .stars(this.stargazersCount)
                .openIssues(this.openIssuesCount)
                .license(this.license != null ? this.license.key : null)
                .build();
    }
}
