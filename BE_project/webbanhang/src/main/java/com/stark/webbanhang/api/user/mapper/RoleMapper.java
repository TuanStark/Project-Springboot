package com.stark.webbanhang.api.user.mapper;

import com.stark.webbanhang.api.user.dto.request.RoleRequest;
import com.stark.webbanhang.api.user.dto.response.RoleResponse;
import com.stark.webbanhang.api.user.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
    void updateRole(@MappingTarget Role role, RoleRequest request);
}
