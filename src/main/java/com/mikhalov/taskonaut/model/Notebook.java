package com.mikhalov.taskonaut.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notebook {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "notebook_id")
    private String id;
    private String name;
    @OneToMany(mappedBy = "notebook", cascade = CascadeType.ALL)
    private List<Note> notes = new ArrayList<>();

    public void addNote(Note note) {
        notes.add(note);
        note.setNotebook(this);
    }

    public void removeNote(Note note) {
        notes.remove(note);
        note.setNotebook(null);
    }

}
