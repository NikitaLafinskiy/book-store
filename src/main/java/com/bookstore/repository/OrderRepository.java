package com.bookstore.repository;

import com.bookstore.entity.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "orderItems")
    @Query("select o from Order o where o.user.id = :id")
    List<Order> findAllByUserId(@Param("id") String id);

    @EntityGraph(attributePaths = "orderItems")
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.id = :orderId")
    Optional<Order> findByIdAndUserId(Long userId, Long orderId);
}
