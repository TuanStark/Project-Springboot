package com.stark.webbanhang.api.user.repository;

import com.stark.webbanhang.api.user.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    // thống kê tổng tiên trong năm
    @Query("SELECT MONTH(o.orderDate) AS month, SUM(o.totalMoney) AS totalEarnings " +
            "FROM Order o " +
            "WHERE YEAR(o.orderDate) = :year " +
            "AND o.status = 0" +
            "GROUP BY MONTH(o.orderDate) " +
            "ORDER BY MONTH(o.orderDate)")
    List<Object[]> findMonthlyEarnings(@Param("year") int year);

    //thống kê tổng tiền trong tháng
}
