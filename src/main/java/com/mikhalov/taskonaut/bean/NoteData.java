package com.mikhalov.taskonaut.bean;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NoteData {
    private String title;
    private String content;
}
