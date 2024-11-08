package com.stark.webbanhang.api.user.dto.request;

import com.stark.webbanhang.api.user.dto.response.ProductResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDto {
    int quantity;
    double totalPrice;
    ProductResponse product;
    double priceDiscount;
    UUID idUser;
}
