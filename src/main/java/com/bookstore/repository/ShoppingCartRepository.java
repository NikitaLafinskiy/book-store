package com.bookstore.repository;

import com.bookstore.entity.ShoppingCart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph(attributePaths = {"cartItems", "user", "cartItems.book"})
    @Query("select c from ShoppingCart c where c.user.email = :email")
    ShoppingCart findByUserEmail(String email);
}
