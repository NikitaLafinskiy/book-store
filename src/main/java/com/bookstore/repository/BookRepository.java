package com.bookstore.repository;

import com.bookstore.entity.Book;
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
}
