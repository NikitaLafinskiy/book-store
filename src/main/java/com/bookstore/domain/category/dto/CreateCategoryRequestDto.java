package com.bookstore.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    @Size(min = 5, max = 255)
    @NotBlank
    private String name;

    @Size(min = 10, max = 9999)
    @NotBlank
    private String description;
}
