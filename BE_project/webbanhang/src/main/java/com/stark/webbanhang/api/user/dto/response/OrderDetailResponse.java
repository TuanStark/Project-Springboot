package com.stark.webbanhang.api.user.dto.response;

import com.stark.webbanhang.api.user.entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
     UUID id;
     double price;
     int quantity;
     double totalMoney;
     UUID orderID;
     ProductResponse product;
}
