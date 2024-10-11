package com.stark.webbanhang.api.user.dto.request;

import com.stark.webbanhang.api.user.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewsRequest {
     String title;
     String content;
     String summary;
}
