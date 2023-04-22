package com.mikhalov.taskonaut.dto;

import com.mikhalov.taskonaut.model.enums.NoteSortOption;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportParamsDTO {
    private NoteSortOption sort = NoteSortOption.LAST_MODIFIED;
    private boolean asc = false;
}