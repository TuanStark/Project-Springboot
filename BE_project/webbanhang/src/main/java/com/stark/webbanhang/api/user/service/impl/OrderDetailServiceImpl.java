package com.stark.webbanhang.api.user.service.impl;

import com.stark.webbanhang.api.user.dto.request.OrderDetailRequest;
import com.stark.webbanhang.api.user.dto.response.OrderDetailResponse;
import com.stark.webbanhang.api.user.dto.response.ProductResponse;
import com.stark.webbanhang.api.user.entity.Order;
import com.stark.webbanhang.api.user.entity.OrderDetail;
import com.stark.webbanhang.api.user.entity.Product;
import com.stark.webbanhang.api.user.mapper.OrderDetailMapper;
import com.stark.webbanhang.api.user.mapper.ProductMapper;
import com.stark.webbanhang.api.user.repository.OrderDetailRepository;
import com.stark.webbanhang.api.user.repository.ProductRepository;
import com.stark.webbanhang.api.user.service.OrderDetailService;
import com.stark.webbanhang.api.user.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    ProductService productService;

    @Override
    @Transactional
    public List<OrderDetailResponse> createOrderDetail(Order order, List<OrderDetailRequest> orderDetailRequests) {
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for(OrderDetailRequest request : orderDetailRequests){
            OrderDetail detail = new OrderDetail();
            detail.setQuantity(request.getQuantity());
            detail.setPrice(request.getPrice());
            detail.setTotalMoney(request.getTotalMoney());
            Product product = productRepository.findById(request.getProductID())
                    .orElseThrow(()-> new RuntimeException("Not found Product!"));

            if (product.getQuantity() < request.getQuantity()) {
                throw new RuntimeException("Not enough quantity for product: " + product.getName());
            }
            product.setQuantity(product.getQuantity() - request.getQuantity());
            productRepository.save(product);

            detail.setProduct(product);
            detail.setOrder(order);

            orderDetailList.add(detail);
        }
        List<OrderDetail> savedOrderDetail = orderDetailRepository.saveAll(orderDetailList);

        List<OrderDetailResponse> listOrderDetailResponse = savedOrderDetail.stream()
                .map(orderDetailMapper::toOrderDetailResponse).collect(Collectors.toList());

        return listOrderDetailResponse;
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

    @Override
    public List<OrderDetailResponse> getOrderDetailResponse(Order order) {
        List<OrderDetailResponse> detailResponse = new ArrayList<>();
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(order.getId());
        for(OrderDetail orderDetail : orderDetailList){
            OrderDetailResponse detail = new OrderDetailResponse();
            detail.setOrderID(order.getId());
            detail.setQuantity(orderDetail.getQuantity());
            detail.setPrice(orderDetail.getPrice());
            detail.setTotalMoney(orderDetail.getTotalMoney());
            Product product = productRepository.findById(orderDetail.getProduct().getId())
                    .orElseThrow(()-> new RuntimeException("Not found Product"));



            ProductResponse productResponse = productService.convertToResponse(product);
            detail.setProduct(productResponse);

            detailResponse.add(detail);
        }
        return detailResponse;
    }

    @Override
    public OrderDetailResponse getOrderDetailByIdOrder(UUID idOrder) {
        List<OrderDetail> detailList = orderDetailRepository.findByOrderId(idOrder);
        return null;
    }


}
