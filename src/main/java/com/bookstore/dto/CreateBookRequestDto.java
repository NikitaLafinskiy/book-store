package com.bookstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotNull
    @Size(min = 5, max = 255)
    private String title;

    @NotNull
    @Size(min = 5, max = 255)
    private String author;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @NotNull
    private String isbn;

    @Size(min = 50, max = 9999)
    private String description;

    private String coverImage;
}
