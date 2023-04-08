package com.mikhalov.taskonaut.mapper;

import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.model.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", uses = {LabelMapper.class}, imports = {LocalDateTime.class})
public interface NoteMapper {


    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(source = "labelDTO", target = "label")
    Note toNote(NoteDTO noteDTO);


    @Mapping(source = "label", target = "labelDTO")
    NoteDTO toNoteDTO(Note note);

    @Mapping(target = "label", ignore = true)
    @Mapping(
            target = "lastModifiedDate",
            expression = "java(LocalDateTime.now())"
    )
    @Mapping(target = "creationDate", ignore = true)
    void updateNote(NoteDTO noteDTO, @MappingTarget Note note);

    default List<NoteDTO> toNoteDTOList(List<Note> notes) {
        return notes.stream()
                .map(this::toNoteDTO)
                .toList();
    }

}