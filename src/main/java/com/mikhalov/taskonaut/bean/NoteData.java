package com.mikhalov.taskonaut.bean;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class NoteData {
    private String id;
    private String title;
    private String content;
}
