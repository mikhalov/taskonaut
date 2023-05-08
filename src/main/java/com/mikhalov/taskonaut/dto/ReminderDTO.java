package com.mikhalov.taskonaut.dto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ReminderDTO(
        @NotEmpty String noteId,
        @NotNull @Future LocalDateTime reminderDateTime) {
}
