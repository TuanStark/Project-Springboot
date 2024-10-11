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
public class PostResponse {
     UUID id;
     String title;
     String description;
     String alias;
     String detail;
     String image;
     String lastName;
     Date createdAt;
}
