package com.stark.webbanhang.api.user.service;

import com.stark.webbanhang.api.user.dto.request.OrderDetailRequest;
import com.stark.webbanhang.api.user.dto.response.OrderDetailResponse;
import com.stark.webbanhang.api.user.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface OrderDetailService {
    public List<OrderDetailResponse> createOrderDetail(Order order, List<OrderDetailRequest> requests);
    public List<OrderDetailResponse> getAll();
    public void deleteOrderDetail(UUID idOrder);
}
