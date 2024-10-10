package com.bookstore.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.user.UserRegistrationRequestDto;
import com.bookstore.dto.user.UserRegistrationResponseDto;
import com.bookstore.entity.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toEntity(UserRegistrationRequestDto userRegistrationRequestDto);

    UserRegistrationResponseDto toDto(User user);
}
