package com.stark.webbanhang.api.user.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stark.webbanhang.utils.formater.time.DateGenerator;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    UUID id;
    String firstName;
    String lastName;
    //String password;
    String email;
    String phone;
    String address;
    int status;
    RoleResponse roles;
    @JsonSerialize(using = DateGenerator.class)
    Date createAt;
}
