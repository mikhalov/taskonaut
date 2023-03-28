package com.mikhalov.taskonaut.mapper;

import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.dto.NotebookDTO;
import com.mikhalov.taskonaut.model.Note;
import com.mikhalov.taskonaut.model.Notebook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

//@Mapper(componentModel = "spring")
public interface NotebookMapper {

    @Mapping(target = "notes", ignore = true)
    Notebook toLabel(NotebookDTO labelDTO);

    NotebookDTO toLabelDTO(Notebook label);

    List<NotebookDTO> toNotebookDTOList(List<Notebook> notes);
}
