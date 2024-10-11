package com.stark.webbanhang.api.user.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
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
     String note;
     LocalDate orderDate;
     double totalMoney;
     int paymentMethods;// chuyển khoản hoặc trả tiền trực tiếp khi nhận hàng
     String userName;
}
