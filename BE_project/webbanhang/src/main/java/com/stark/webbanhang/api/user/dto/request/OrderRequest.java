package com.stark.webbanhang.api.user.dto.request;

import com.stark.webbanhang.api.user.entity.User;
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
public class OrderRequest {
     int status;
     String note;
     LocalDate orderDate;
     double totalMoney;
     int paymentMethods;// chuyển khoản hoặc trả tiền trực tiếp khi nhạn hàng
     UUID userID;
     List<OrderDetailRequest> orderDetails;
}
