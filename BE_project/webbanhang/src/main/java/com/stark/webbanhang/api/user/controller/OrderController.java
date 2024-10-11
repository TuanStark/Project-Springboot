package com.stark.webbanhang.api.user.controller;

import com.stark.webbanhang.api.user.dto.request.OrderRequest;
import com.stark.webbanhang.api.user.dto.response.OrderResponse;
import com.stark.webbanhang.api.user.service.OrderService;
import com.stark.webbanhang.helper.base.response.ResponseObject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping("/add")
    public ResponseObject<OrderResponse> create(
            @RequestBody OrderRequest request,
            @RequestHeader String header){

        OrderResponse orderResponse = orderService.createOder(request,header);
        return ResponseObject.<OrderResponse>builder()
                .data(orderResponse)
                .build();
    }

}
