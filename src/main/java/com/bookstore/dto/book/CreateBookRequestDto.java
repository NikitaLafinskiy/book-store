package com.bookstore.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

@Data
public class CreateBookRequestDto {
    @NotBlank
    @Size(min = 5, max = 255)
    private String title;

    @NotBlank
    @Size(min = 5, max = 255)
    private String author;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @NotBlank
    @ISBN
    private String isbn;

    @Size(min = 50, max = 9999)
    private String description;

    private String coverImage;
}
