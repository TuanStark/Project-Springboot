package com.stark.webbanhang.api.user.mapper;

import com.stark.webbanhang.api.user.dto.request.GalleryRequest;
import com.stark.webbanhang.api.user.dto.request.OrderDetailRequest;
import com.stark.webbanhang.api.user.dto.response.GalleryResponse;
import com.stark.webbanhang.api.user.dto.response.OrderDetailResponse;
import com.stark.webbanhang.api.user.dto.response.ProductResponse;
import com.stark.webbanhang.api.user.entity.Gallery;
import com.stark.webbanhang.api.user.entity.OrderDetail;
import com.stark.webbanhang.api.user.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetail toOrderDetail(OrderDetailRequest request);
    @Mapping(source = "product", target = "product")
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);
    void updateOrderDetailResponse(@MappingTarget OrderDetail orderDetail, OrderDetailRequest request);
}
