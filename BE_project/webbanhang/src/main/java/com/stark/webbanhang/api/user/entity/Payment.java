package com.stark.webbanhang.api.user.entity;

import com.stark.webbanhang.helper.base.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "payment")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Payment extends BaseEntity {
    private double amount;
    @Column(name = "payment_method")
    private int paymentMethod;
    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
