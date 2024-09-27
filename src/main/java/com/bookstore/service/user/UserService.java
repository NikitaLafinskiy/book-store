package com.bookstore.service.user;

import com.bookstore.dto.user.UserRegistrationRequestDto;
import com.bookstore.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto save(UserRegistrationRequestDto userRegistrationRequestDto);
}
