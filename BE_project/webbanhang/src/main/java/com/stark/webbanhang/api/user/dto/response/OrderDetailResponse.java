package com.stark.webbanhang.api.user.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
     String productName;
     UUID orderID;
     List<ProductResponse> product;
}
