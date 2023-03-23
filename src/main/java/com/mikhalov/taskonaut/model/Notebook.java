package com.mikhalov.taskonaut.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notebook {
    @Id
    @GeneratedValue(generator = "UUID")
    private String id;
    private String name;
    @OneToMany(mappedBy = "notebook", cascade = CascadeType.ALL)
    private List<Note> notes;

}
