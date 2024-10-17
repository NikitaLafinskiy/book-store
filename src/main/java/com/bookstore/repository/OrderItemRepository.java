package com.bookstore.repository;

import com.bookstore.entity.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi FROM OrderItem oi WHERE oi.id = :id "
            + "AND oi.order.id = :orderId "
            + "AND oi.order.user.id = :userId")
    Optional<OrderItem> findByIdAndOrderIdAndUserId(Long id, Long orderId, Long userId);
}
