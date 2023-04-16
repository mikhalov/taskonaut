package com.mikhalov.taskonaut.dto;

import com.mikhalov.taskonaut.model.enums.NoteSortOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class SortAndPageDTO {
    @ToString.Exclude
    private final NoteSortOption[] sortOptions = NoteSortOption.values();
    private NoteSortOption sort = NoteSortOption.LAST_MODIFIED;
    @ToString.Exclude
    private boolean asc = false;
    private int page = 0;
    private int size = 20;
    @ToString.Exclude
    private int totalPages;


}

