package com.bookstore.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotBlank
    @Size(min = 4, max = 64)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 64)
    private String password;
}
