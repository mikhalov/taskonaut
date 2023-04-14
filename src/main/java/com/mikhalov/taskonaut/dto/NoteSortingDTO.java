package com.mikhalov.taskonaut.dto;

import com.mikhalov.taskonaut.model.enums.NoteSortOption;

import java.util.Arrays;
import java.util.Objects;

public record NoteSortingDTO(NoteSortOption[] sortOptions, boolean ascending) {

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (NoteSortingDTO) obj;
        return Arrays.equals(this.sortOptions, that.sortOptions) &&
                this.ascending == that.ascending;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(sortOptions), ascending);
    }

    @Override
    public String toString() {
        return "NoteSortingDTO[" +
                "sortOptions=" + Arrays.toString(sortOptions) + ", " +
                "ascending=" + ascending + ']';
    }


}
