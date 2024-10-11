package com.bookstore.domain.auth.controller;

import com.bookstore.domain.auth.service.AuthenticationService;
import com.bookstore.domain.user.dto.UserLoginRequestDto;
import com.bookstore.domain.user.dto.UserLoginResponseDto;
import com.bookstore.domain.user.dto.UserRegistrationRequestDto;
import com.bookstore.domain.user.dto.UserRegistrationResponseDto;
import com.bookstore.domain.user.service.UserService;
import com.bookstore.exception.RegistrationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Endpoints for user authentication")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Operation(summary = "Register a new user", responses = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "User already exists")
    })
    @PostMapping("/registration")
    public UserRegistrationResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException {
        return userService.register(userRegistrationRequestDto);
    }

    @Operation(summary = "Login using the password and the username", responses = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully")
    })
    @PostMapping("/login")
    public UserLoginResponseDto login(
            @RequestBody @Valid UserLoginRequestDto userLoginRequestDto) {
        return authenticationService.login(userLoginRequestDto);
    }
}
