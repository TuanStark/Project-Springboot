package com.stark.webbanhang.api.user.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewsResponse {
     UUID id;
     String title;
     String summary;
     String alias;
     String content;
     String image;
     String lastName;
     Date createdAt;
}
