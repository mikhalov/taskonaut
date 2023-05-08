package com.mikhalov.taskonaut.dto;

import java.io.Serializable;

public record NoteForTelegramDTO(String noteId, Long chatId) implements Serializable {
}
