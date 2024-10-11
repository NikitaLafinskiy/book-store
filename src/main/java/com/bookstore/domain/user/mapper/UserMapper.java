package com.bookstore.domain.user.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.domain.user.dto.UserRegistrationRequestDto;
import com.bookstore.domain.user.dto.UserRegistrationResponseDto;
import com.bookstore.domain.user.entity.User;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toEntity(UserRegistrationRequestDto userRegistrationRequestDto);

    UserRegistrationResponseDto toDto(User user);

    @Named("userById")
    default User userById(Long userId) {
        return Optional.ofNullable(userId)
                .stream()
                .map(User::new)
                .findFirst()
                .orElse(null);
    }
}
