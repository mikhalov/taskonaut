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
        return new Note(noteData.getTitle(), noteData.getContent());
    }

    default NoteData toNoteData(Note note) {
        return new NoteData( note.getTitle(), note.getContent());
    }

    List<NoteData> toNoteDataList(List<Note> notes);

    void updateNote(NoteData noteData, @MappingTarget Note note);

}