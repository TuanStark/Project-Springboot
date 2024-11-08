package com.stark.webbanhang.api.user.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
     UUID id;
     int status;
     String OrderId;
     String note;
     Date orderDate;
     double totalMoney;
     int paymentMethods;
     UserResponse userID;
     List<OrderDetailResponse> orderDetails;
}
