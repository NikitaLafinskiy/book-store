package com.bookstore.domain.auth.service;

import com.bookstore.domain.user.dto.UserLoginRequestDto;
import com.bookstore.domain.user.dto.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto);
}
