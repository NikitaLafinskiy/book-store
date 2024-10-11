package com.bookstore.domain.order.repository;

import com.bookstore.domain.order.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "orderItems")
    @Query("select o from Order o where o.user.email = :email")
    List<Order> findByUserEmail(@Param("email") String email);
}
