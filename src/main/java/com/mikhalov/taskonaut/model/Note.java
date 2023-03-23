package com.mikhalov.taskonaut.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private String id;
    private String title;
    @Lob
    private String content;
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
    @ManyToOne
    @JoinColumn(name = "notebook_id")
    private Notebook notebook;
}
