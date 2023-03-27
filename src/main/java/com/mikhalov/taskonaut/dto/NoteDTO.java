package com.mikhalov.taskonaut.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NoteDTO {
    private String id;
    private String title;
    private String content;
}
