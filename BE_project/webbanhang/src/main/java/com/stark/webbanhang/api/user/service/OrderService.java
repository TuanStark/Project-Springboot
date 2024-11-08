package com.stark.webbanhang.api.user.service;


import com.stark.webbanhang.api.user.dto.request.OrderRequest;
import com.stark.webbanhang.api.user.dto.response.OrderResponse;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.entity.Order;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface OrderService {
     OrderResponse createOder(OrderRequest orderRequest);
     Order findById (UUID orderId);
     PageResponse<OrderResponse> getAllOrder(int page, int size, int limit);
     OrderResponse updateOrder(UUID idOrder,int status);
     void deleteOrder(UUID idOrder);
     OrderResponse getOrderById(UUID orderId);
}
