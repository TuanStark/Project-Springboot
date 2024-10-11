package com.stark.webbanhang.api.user.mapper;

import com.stark.webbanhang.api.user.dto.request.UserRequest;
import com.stark.webbanhang.api.user.dto.request.UserUpdateRequest;
import com.stark.webbanhang.api.user.dto.response.UserResponse;
import com.stark.webbanhang.api.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest request);
    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
