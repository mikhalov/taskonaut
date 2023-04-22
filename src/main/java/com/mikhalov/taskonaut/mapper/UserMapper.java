package com.mikhalov.taskonaut.mapper;

import com.mikhalov.taskonaut.dto.SignInDTO;
import com.mikhalov.taskonaut.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "telegramChatId", ignore = true)
    User toUser(SignInDTO signInDTO);
}
