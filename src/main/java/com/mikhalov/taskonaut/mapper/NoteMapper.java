package com.mikhalov.taskonaut.mapper;

import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.model.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoteMapper {


    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "notebook", ignore = true)
    Note toNote(NoteDTO noteDTO);

    NoteDTO toNoteData(Note note);

    List<NoteDTO> toNoteDataList(List<Note> notes);

    void updateNote(NoteDTO noteDTO, @MappingTarget Note note);

}