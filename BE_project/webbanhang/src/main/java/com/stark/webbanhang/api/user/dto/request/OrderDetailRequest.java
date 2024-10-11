package com.stark.webbanhang.api.user.dto.request;

import com.stark.webbanhang.api.user.entity.Order;
import com.stark.webbanhang.api.user.entity.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailRequest {
     double price;
     int quantity;
     double totalMoney;
     UUID productID;
     UUID orderID;
}
