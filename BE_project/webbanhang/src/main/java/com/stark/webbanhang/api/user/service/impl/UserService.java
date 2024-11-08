package com.stark.webbanhang.api.user.service.impl;


import com.stark.webbanhang.api.user.dto.request.UserRequest;
import com.stark.webbanhang.api.user.dto.request.UserUpdateRequest;
import com.stark.webbanhang.api.user.dto.response.CategoryResponse;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.dto.response.UserResponse;
import com.stark.webbanhang.api.user.entity.Category;
import com.stark.webbanhang.api.user.entity.Role;
import com.stark.webbanhang.api.user.entity.User;
import com.stark.webbanhang.api.user.mapper.RoleMapper;
import com.stark.webbanhang.api.user.mapper.UserMapper;
import com.stark.webbanhang.api.user.repository.RoleRepository;
import com.stark.webbanhang.api.user.repository.UserRepository;
import com.stark.webbanhang.helper.exception.AppException;
import com.stark.webbanhang.helper.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor// thay co contructer nó sẽ tự động inject
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)// những cái không khai báo gì thì đưa về final hết
public class UserService {
     UserRepository userRepository;
     UserMapper userMapper;// cái này đã được component trong spring
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    AuthenticationService authenticationService;

    @Transactional
    public UserResponse createRequest(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User already exists");
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(1);
        user.setCreatedAt(new Date());

        Role role = roleRepository.findByName("USER")
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        user.setRole(role);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // Lấy thông tin của user đang đăng nhập hiện tại
    public UserResponse getMyInfo( String authHeader ) {
        String token = authHeader.substring(7); // Lấy token từ header (bỏ "Bearer ")
        UUID idUser = authenticationService.getCurrentUserId(token);

        User byUserName = userRepository.findById(idUser)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));

        return userMapper.toUserResponse(byUserName);
    }

    @Transactional
    public UserResponse updateUser(UUID userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));

        userMapper.updateUser(user, request);
        if(request.getPassword() == null){
            user.setPassword(user.getPassword());
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUpdatedAt(new Date());
        user.setStatus(request.getStatus());
        Role role = roleRepository.findById(request.getRoles())
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        user.setRole(role);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // @PreAuthorize("hasAuthority('CREATE_DATA')") // cái này dùng cho permission
    //@PreAuthorize("hasRole('ADMIN')")
    public PageResponse<UserResponse> getAllUser(int page, int size){
        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(page-1,size,sort);
        Page<User> pageData = userRepository.findAll(pageable);

        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPage(pageData.getTotalPages())
                .totalElement(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(this::convertToResponse).toList())
                .build();
    }

   // @PostAuthorize("returnObject.username == authentication.name")// chỉ user nào đang đăng nhập mới có thể xem thông tin của
    // chính id của mình , còn lại thì k được xem id của người khác
    public UserResponse getUser(UUID id){
        return this.convertToResponse(userRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_EXISTED)));
    }

    public UserResponse convertToResponse(User user){
        UserResponse response = userMapper.toUserResponse(user);
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        response.setRoles(roleMapper.toRoleResponse(user.getRole()));
        return response;
    }

    @Transactional
    public UserResponse deleteUser(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        user.setStatus(0);
        UserResponse response = userMapper.toUserResponse(user);
       response.setStatus(user.getStatus());

        return response;
    }
}
