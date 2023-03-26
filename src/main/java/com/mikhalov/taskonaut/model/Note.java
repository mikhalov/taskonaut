package com.mikhalov.taskonaut.model;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Note {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "note_id")
    private String id;
    private String title;
    private String content;
    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();
    private LocalDateTime lastModifiedDate = LocalDateTime.now();
    @ManyToOne
    @JoinColumn(name = "notebook_id")
    private Notebook notebook;

}

