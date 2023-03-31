package com.mikhalov.taskonaut.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Label {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "label_id")
    private String id;
    @Column(unique = true, nullable = false)

    private String name;
    @OneToMany(mappedBy = "label", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Note> notes = new ArrayList<>();

    public void addNote(Note note) {
        notes.add(note);
        note.setLabel(this);
    }

    public void removeNote(Note note) {
        notes.remove(note);
        note.setLabel(null);
    }

}
