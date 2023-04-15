package com.mikhalov.taskonaut.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoteSortOption {
    CREATION_DATE("creationDate", "Creation date"),
    TITLE("title", "Title"),
    LAST_MODIFIED("lastModifiedDate", "Last Modified");

    private final String sortProperty;
    private final String value;



}