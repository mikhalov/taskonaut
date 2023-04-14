package com.mikhalov.taskonaut.model.enums;

import lombok.Getter;

@Getter
public enum NoteSortOption {
    CREATION_DATE("creationDate"),
    TITLE("title"),
    LAST_MODIFIED("lastModifiedDate");

    private final String sortProperty;

    NoteSortOption(String sortProperty) {
        this.sortProperty = sortProperty;
    }

}