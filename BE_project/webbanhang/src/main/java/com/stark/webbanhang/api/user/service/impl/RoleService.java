package com.stark.webbanhang.api.user.service.impl;


import com.stark.webbanhang.api.user.dto.request.RoleRequest;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.dto.response.RoleResponse;
import com.stark.webbanhang.api.user.dto.response.UserResponse;
import com.stark.webbanhang.api.user.entity.Role;
import com.stark.webbanhang.api.user.entity.User;
import com.stark.webbanhang.api.user.mapper.RoleMapper;
import com.stark.webbanhang.api.user.repository.RoleRepository;
import com.stark.webbanhang.helper.exception.AppException;
import com.stark.webbanhang.helper.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor// thay co contructer nó sẽ tự động inject
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)// những cái không khai báo gì thì đưa về final hết
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    @Transactional
    public RoleResponse create(RoleRequest request){
        var role = roleMapper.toRole(request);
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public PageResponse<RoleResponse> getAll(int page, int size){
        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(page-1,size,sort);
        Page<Role> pageData = roleRepository.findAll(pageable);

        return PageResponse.<RoleResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPage(pageData.getTotalPages())
                .totalElement(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(roleMapper::toRoleResponse).toList())
                .build();
    }
    @Transactional
    public void delete(UUID roleID){
        Role role = roleRepository.findById(roleID)
                        .orElseThrow(()-> new AppException(ErrorCode.ROLE_NOT_FOUND));
        roleRepository.deleteById(roleID);
    }
}
