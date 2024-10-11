package com.stark.webbanhang.api.user.dto.request;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    //@Size(min = 3, message = "USERNAME_INVALID")
    String firstName;
    String lastName;
    @Size(min = 8, message = "INVALID_PASSWORD")
    //@Pattern(regexp = "^(?=.*[0-9])(?=.*[@#$%^&+=!])(?=\\S+$).*$",message = "Mật khẩu phải chứa ít nhất 1 chữ số và 1 ký tự đặc biệt")
    String password;
    String email;
    String phone;
    int status;
    UUID roles;
    String address;
}
