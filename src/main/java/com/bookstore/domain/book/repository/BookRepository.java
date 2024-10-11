package com.bookstore.domain.book.repository;

import com.bookstore.domain.book.dto.BookPriceProjectionDto;
import com.bookstore.domain.book.entity.Book;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("select b from Book b left join fetch b.categories c where c.id = :categoryId")
    Page<Book> findAllByCategoryId(Pageable pageable, @Param("categoryId") Long categoryId);

    @Query("select b from Book b left join fetch b.categories")
    Page<Book> findAllWithCategories(Pageable pageable);

    @Query("select new com.bookstore.domain.book.dto.BookPriceProjectionDto(b.id, b.price) "
            + "from Book b where b.id in :bookIds")
    List<BookPriceProjectionDto> findAllByBookIds(List<Long> bookIds);
}
