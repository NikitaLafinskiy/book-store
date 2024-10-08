package com.bookstore.repository;

import com.bookstore.entity.Book;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
//    @EntityGraph(attributePaths = "categories")
//    @Query("select b from Book b inner join fetch b.categories c where c.id = :categoryId") ??
    List<Book> findAllByCategoryId(Pageable pageable, Long categoryId);
}
