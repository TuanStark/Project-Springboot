package com.stark.webbanhang.api.user.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GalleryRequest {
    private String image;
    private boolean isDefault;
}
