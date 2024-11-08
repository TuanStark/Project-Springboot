package com.stark.webbanhang.api.admin.controller;

import com.stark.webbanhang.api.user.dto.response.OrderResponse;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.service.OrderDetailService;
import com.stark.webbanhang.api.user.service.OrderService;
import com.stark.webbanhang.helper.base.constant.StatusMessage;
import com.stark.webbanhang.helper.base.response.ResponseObject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrdersController {
    OrderService orderService;
    OrderDetailService orderDetailService;

    @GetMapping("/getAll")
    public ResponseObject<PageResponse> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "limit", required = false) Integer limit){
        if (size == null) {
            size = Integer.MAX_VALUE;
        }
        PageResponse<OrderResponse> response = orderService.getAllOrder(page, size,limit);
        return ResponseObject.<PageResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(response)
                .build();
    }

    @PutMapping("/update/{id}")
    public ResponseObject<OrderResponse> update(
            @PathVariable UUID id,
            @RequestParam int status){
        var result = orderService.updateOrder(id, status);
        return ResponseObject.<OrderResponse>builder()
                .code(200)
                .message(StatusMessage.SUCCESS)
                .data(result)
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseObject<Void> delete(@PathVariable UUID id){
        orderService.deleteOrder(id);
        return ResponseObject.<Void>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .build();
    }

    @GetMapping("/getById/{idOrder}")
    public ResponseObject<OrderResponse> getAll(@PathVariable UUID idOrder){
        OrderResponse response = orderService.getOrderById(idOrder);
        return ResponseObject.<OrderResponse>builder()
                .data(response)
                .code(200)
                .message(StatusMessage.SUCCESS)
                .build();
    }

}
