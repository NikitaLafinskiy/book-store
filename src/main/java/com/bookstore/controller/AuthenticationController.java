package com.bookstore.controller;

import com.bookstore.dto.user.UserLoginRequestDto;
import com.bookstore.dto.user.UserLoginResponseDto;
import com.bookstore.dto.user.UserRegistrationRequestDto;
import com.bookstore.dto.user.UserRegistrationResponseDto;
import com.bookstore.exception.RegistrationException;
import com.bookstore.service.auth.AuthenticationService;
import com.bookstore.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
