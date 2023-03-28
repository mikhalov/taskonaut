package com.mikhalov.taskonaut.mapper;

import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface NoteManageMapper extends NoteMapper, NotebookMapper{
}
