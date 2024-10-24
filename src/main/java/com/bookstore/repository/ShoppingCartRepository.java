package com.bookstore.repository;

import com.bookstore.entity.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Override
    @EntityGraph(attributePaths = {"cartItems", "user", "cartItems.book"})
    Optional<ShoppingCart> findById(Long id);
}
