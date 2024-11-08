package com.stark.webbanhang.api.user.controller;

import com.stark.webbanhang.api.user.dto.request.OrderDetailRequest;
import com.stark.webbanhang.api.user.dto.request.OrderRequest;
import com.stark.webbanhang.api.user.dto.response.OrderResponse;
import com.stark.webbanhang.api.user.service.OrderService;
import com.stark.webbanhang.helper.base.response.ResponseObject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping("/add")
    public ResponseObject<OrderResponse> create(@RequestBody OrderRequest orderRequest){

        OrderResponse orderResponse = orderService.createOder(orderRequest);
        return ResponseObject.<OrderResponse>builder()
                .data(orderResponse)
                .build();
    }

}
