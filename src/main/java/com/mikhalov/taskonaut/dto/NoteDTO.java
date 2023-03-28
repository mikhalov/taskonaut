package com.mikhalov.taskonaut.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoteDTO {
    private String id;
    private String title;
    private String content;
    private NotebookDTO notebookDTO;
}
