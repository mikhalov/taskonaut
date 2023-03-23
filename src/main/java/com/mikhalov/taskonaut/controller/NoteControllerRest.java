package com.mikhalov.taskonaut.controller;

//import com.mikhalov.taskonaut.model.Note;
//import com.mikhalov.taskonaut.service.NoteService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.persistence.EntityNotFoundException;
//import java.util.List;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/note")
//public class NoteControllerRest {
//
//    private final NoteService noteService;
//
//    @GetMapping
//    public ResponseEntity<List<Note>> getAllNotes() {
//        List<Note> notes = noteService.getAllNotes();
//        return new ResponseEntity<>(notes, HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Note> getNoteById(@PathVariable String id) {
//        Note note = noteService.getNoteById(id).orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
//        return new ResponseEntity<>(note, HttpStatus.OK);
//    }
//
//    @PostMapping
//    public ResponseEntity<Note> createNote(@RequestBody Note note) {
//        Note createdNote = noteService.createNote(note);
//        return new ResponseEntity<>(createdNote, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Note> updateNote(@PathVariable String id, @RequestBody Note note) {
//        Note updatedNote = noteService.updateNote(id, note);
//        return new ResponseEntity<>(updatedNote, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteNote(@PathVariable String id) {
//        noteService.deleteNote(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//}
