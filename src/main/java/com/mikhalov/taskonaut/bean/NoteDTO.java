package com.mikhalov.taskonaut.bean;

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
