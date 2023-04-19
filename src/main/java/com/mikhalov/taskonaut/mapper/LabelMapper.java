package com.mikhalov.taskonaut.mapper;

import com.mikhalov.taskonaut.dto.LabelDTO;
import com.mikhalov.taskonaut.model.Label;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LabelMapper {

    @Mapping(target = "notes", ignore = true)
    Label toLabel(LabelDTO labelDTO);

    LabelDTO toLabelDTO(Label label);

    default List<LabelDTO> toLabelDTOList(List<Label> labels) {
        return labels.stream()
                .map(this::toLabelDTO)
                .toList();
    }

}
