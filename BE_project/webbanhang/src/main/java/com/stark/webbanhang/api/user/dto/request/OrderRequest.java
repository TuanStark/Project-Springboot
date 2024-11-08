package com.stark.webbanhang.api.user.dto.request;

import com.stark.webbanhang.api.user.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
     int status;
     String note;
     Date orderDate;
     double totalMoney;
     int paymentMethods;
     UUID userID;
     List<OrderDetailRequest> orderDetails;
}
