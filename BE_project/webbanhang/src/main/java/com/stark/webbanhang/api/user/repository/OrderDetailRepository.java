package com.stark.webbanhang.api.user.repository;

import com.stark.webbanhang.api.user.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    void deleteByProductId(UUID id);
    List<OrderDetail> findByOrderId(UUID id);
}
