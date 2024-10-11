package com.stark.webbanhang.api.user.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
     String name;
     String description;
     double price;
     int discount;
     String ingredient;
     String size;
     int weight;
     int quantity;
     int status;
     String note;
     String content;
     boolean hot;
     boolean newProduct;
     boolean promotionProduct;
     UUID categoryID;
     @NotNull
     private List<MultipartFile> images; // Danh sách ảnh sản phẩm
     @NotNull
     private List<Boolean> isDefaultList; // Danh sách xác định ảnh chính
}
