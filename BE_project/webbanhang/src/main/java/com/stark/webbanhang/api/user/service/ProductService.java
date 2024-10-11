package com.stark.webbanhang.api.user.service;

import com.stark.webbanhang.api.user.dto.request.ProductRequest;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public interface ProductService {
    public ProductResponse createProduct(ProductRequest request);
    public ProductResponse updateProduct(UUID id,ProductRequest request);
    public PageResponse<ProductResponse> getAllProduct(int page, int size);
    public void deleteProduct(UUID id);
    public ProductResponse getProductById(UUID id);
    public PageResponse<ProductResponse> getProductByIdCategory(int page, int size, UUID categoryId);
}
