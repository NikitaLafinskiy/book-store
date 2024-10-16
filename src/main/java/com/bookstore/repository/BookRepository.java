package com.bookstore.repository;

import com.bookstore.dto.book.BookPriceProjectionDto;
import com.bookstore.entity.Book;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.categories c WHERE c.id = :categoryId")
    Page<Book> findAllByCategoryId(Pageable pageable, @Param("categoryId") Long categoryId);

    @EntityGraph(attributePaths = "categories")
    Page<Book> findAll(Pageable pageable);

    @Query("select new com.bookstore.dto.book.BookPriceProjectionDto(b.id, b.price) "
            + "from Book b where b.id in :bookIds")
    List<BookPriceProjectionDto> findAllByBookIds(List<Long> bookIds);
}
