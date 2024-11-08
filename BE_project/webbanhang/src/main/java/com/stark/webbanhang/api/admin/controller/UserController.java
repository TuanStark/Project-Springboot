package com.stark.webbanhang.api.admin.controller;
import com.stark.webbanhang.api.user.dto.request.UserRequest;
import com.stark.webbanhang.api.user.dto.request.UserUpdateRequest;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.dto.response.UserResponse;
import com.stark.webbanhang.api.user.service.impl.UserService;
import com.stark.webbanhang.helper.base.constant.StatusMessage;
import com.stark.webbanhang.helper.base.response.ResponseObject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping("/getAll")
    ResponseObject<PageResponse> getUser(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false) Integer size){
        if (size == null) {
            size = Integer.MAX_VALUE;
        }
        return ResponseObject.<PageResponse>builder()
                .data(userService.getAllUser(page,size))
                .build();
    }

    @GetMapping("/myInfo")
    ResponseObject<UserResponse> getMyInfo(@RequestHeader("Authorization") String authHeader){
        return ResponseObject.<UserResponse>builder()
                .data(userService.getMyInfo(authHeader))
                .build();
    }

    @GetMapping("/getById/{userId}")
    ResponseObject<UserResponse> getUser(@PathVariable("userId") UUID userId){

        return ResponseObject.<UserResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(userService.getUser(userId))
                .build();
    }

    @PutMapping("/update/{userId}")
    ResponseObject<UserResponse> updateUser(@PathVariable UUID userId, @RequestBody UserUpdateRequest request){
        return ResponseObject.<UserResponse>builder()
                .code(200)
                .message(StatusMessage.SUCCESS)
                .data(userService.updateUser(userId,request))
                .build();
    }

    @DeleteMapping("/delete/{userId}")
    ResponseObject<Void> deleteUser(@PathVariable UUID userId){
        userService.deleteUser(userId);
        return ResponseObject.<Void>builder()
                .code(200)
                .message(StatusMessage.SUCCESS)
                .build();
    }

    @PostMapping("/add")
    ResponseObject<UserResponse> create(@ModelAttribute UserRequest request){

        return ResponseObject.<UserResponse>builder()
                .data(userService.createRequest(request))
                .code(200)
                .message(StatusMessage.SUCCESS)
                .build();
    }
}
