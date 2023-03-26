package com.mikhalov.taskonaut.mapper;

import com.mikhalov.taskonaut.bean.NoteData;
import com.mikhalov.taskonaut.model.Note;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface NoteMapper {

    NoteMapper INSTANCE = Mappers.getMapper(NoteMapper.class);

    default Note toNote(NoteData noteData) {
        return Note.builder()
                .id(noteData.getId())
                .title(noteData.getTitle())
                .content(noteData.getContent())
                .build();
    }

    default NoteData toNoteData(Note note) {
        return NoteData.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .build();
    }

    List<NoteData> toNoteDataList(List<Note> notes);

    void updateNote(NoteData noteData, @MappingTarget Note note);

}