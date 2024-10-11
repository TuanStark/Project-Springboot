package com.stark.webbanhang.api.admin.controller;

import com.stark.webbanhang.api.user.dto.response.OrderDetailResponse;
import com.stark.webbanhang.api.user.service.OrderDetailService;
import com.stark.webbanhang.helper.base.response.ResponseObject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/detail")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderDetailController {
    OrderDetailService orderDetailService;

   /* @GetMapping("/getAll")
    public ResponseObject<OrderDetailResponse> getAll(){
        var orderDetail =orderDetailService.get
    }*/
}
