package com.stark.webbanhang.api.admin.controller;

import com.stark.webbanhang.api.user.dto.request.RoleRequest;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.dto.response.RoleResponse;
import com.stark.webbanhang.api.user.service.impl.RoleService;
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
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping("/add")
    ResponseObject<RoleResponse> create(@RequestBody RoleRequest request){
        return ResponseObject.<RoleResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(roleService.create(request))
                .build();
    }

    @GetMapping("/getAll")
    ResponseObject<PageResponse> getAll(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                        @RequestParam(value = "size", required = false) Integer size){
        return ResponseObject.<PageResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(roleService.getAll(page, size))
                .build();
    }

    @DeleteMapping("/{roleId}")
    ResponseObject<Void> delete(@PathVariable UUID roleId){
        roleService.delete(roleId);
        return ResponseObject.<Void>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .build();
    }
}
