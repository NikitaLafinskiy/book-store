package com.bookstore.service.user;

import com.bookstore.dto.user.UserRegistrationRequestDto;
import com.bookstore.dto.user.UserRegistrationResponseDto;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto);
}
