package com.mikhalov.taskonaut.dto;

import com.mikhalov.taskonaut.model.enums.NoteSortOption;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class NoteSortingDTO {

    private final NoteSortOption[] sortOptions;
    private final NoteSortOption currentSorting;
    private final boolean ascending;

}
