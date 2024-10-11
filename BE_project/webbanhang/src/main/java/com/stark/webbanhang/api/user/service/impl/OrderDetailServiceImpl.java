package com.stark.webbanhang.api.user.service.impl;

import com.stark.webbanhang.api.user.dto.request.OrderDetailRequest;
import com.stark.webbanhang.api.user.dto.response.GalleryResponse;
import com.stark.webbanhang.api.user.dto.response.OrderDetailResponse;
import com.stark.webbanhang.api.user.entity.Gallery;
import com.stark.webbanhang.api.user.entity.Order;
import com.stark.webbanhang.api.user.entity.OrderDetail;
import com.stark.webbanhang.api.user.entity.Product;
import com.stark.webbanhang.api.user.mapper.OrderDetailMapper;
import com.stark.webbanhang.api.user.repository.OrderDetailRepository;
import com.stark.webbanhang.api.user.repository.OrderRepository;
import com.stark.webbanhang.api.user.repository.ProductRepository;
import com.stark.webbanhang.api.user.service.OrderDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailServiceImpl implements OrderDetailService {
    OrderDetailRepository orderDetailRepository;
    OrderDetailMapper orderDetailMapper;
    ProductRepository productRepository;
    OrderRepository orderRepository;

    @Override
    public List<OrderDetailResponse> createOrderDetail(Order order, List<OrderDetailRequest> orderDetailRequests) {
        List<OrderDetail> orderDetails = orderDetailRequests.stream().map(orderDetailRequest -> {
            // Kiểm tra sản phẩm tồn tại
            Product product = productRepository.findById(orderDetailRequest.getProductID())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + orderDetailRequest.getProductID()));

            // Tạo OrderDetail
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setPrice(orderDetailRequest.getPrice());
            orderDetail.setQuantity(orderDetailRequest.getQuantity());
            orderDetail.setTotalMoney(orderDetailRequest.getTotalMoney());

            return orderDetail;
        }).collect(Collectors.toList());

        List<OrderDetail> savedOrderDetail = orderDetailRepository.saveAll(orderDetails);

        List<OrderDetailResponse> listGalleryResponse = savedOrderDetail.stream()
                .map(orderDetailMapper::toOrderDetailResponse)
                .collect(Collectors.toList());

        return listGalleryResponse;
    }

    @Override
    public List<OrderDetailResponse> getAll() {
        return List.of();
    }

    @Override
    public void deleteOrderDetail(UUID idOrder) {
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(idOrder);
        orderDetailRepository.deleteAll(orderDetailList);
    }


}
