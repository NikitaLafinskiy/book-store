package com.bookstore.dto.book;

import java.math.BigDecimal;

public record BookPriceProjectionDto(Long id, BigDecimal price) {
}
