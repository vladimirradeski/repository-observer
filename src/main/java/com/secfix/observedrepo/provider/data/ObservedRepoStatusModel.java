package com.secfix.observedrepo.provider.data;

import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;

enum ObservedRepoStatusModel {
    ACTIVE,
    DELETED,
    INVALID;

    public ObservedRepoStatus toDomain() {
        return switch (this) {
            case ACTIVE -> ObservedRepoStatus.ACTIVE;
            case DELETED -> ObservedRepoStatus.DELETED;
            case INVALID -> ObservedRepoStatus.INVALID;
        };
    }

    public static ObservedRepoStatusModel fromDomain(ObservedRepoStatus status) {
        return switch (status) {
            case ACTIVE -> ACTIVE;
            case DELETED -> DELETED;
            case INVALID -> INVALID;
            case null -> null;
        };
    }
}
