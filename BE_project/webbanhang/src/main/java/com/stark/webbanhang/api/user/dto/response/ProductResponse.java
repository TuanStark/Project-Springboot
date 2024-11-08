package com.stark.webbanhang.api.user.dto.response;

import com.stark.webbanhang.api.user.dto.request.GalleryRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
     UUID id;
     String name;
     String description;
     double price;
     int discount;
     String ingredient;
     String size;
     String image;
     int weight;
     int quantity;
     int status;
     String note;
     String content;
     boolean hot;
     boolean newProduct;
     boolean promotionProduct;
     CategoryResponse category;
     private Date createdAt;
     @NotNull
     private List<GalleryResponse> images;
}
