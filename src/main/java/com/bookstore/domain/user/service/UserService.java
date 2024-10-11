package com.bookstore.domain.user.service;

import com.bookstore.domain.user.dto.UserRegistrationRequestDto;
import com.bookstore.domain.user.dto.UserRegistrationResponseDto;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto);
}
