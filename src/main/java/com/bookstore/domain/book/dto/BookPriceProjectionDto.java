package com.bookstore.domain.book.dto;

import java.math.BigDecimal;

public record BookPriceProjectionDto(Long id, BigDecimal price) {
}
