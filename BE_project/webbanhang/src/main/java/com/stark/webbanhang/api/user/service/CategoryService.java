package com.stark.webbanhang.api.user.service;

import com.stark.webbanhang.api.user.dto.request.CategoryRequest;
import com.stark.webbanhang.api.user.dto.response.CategoryResponse;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface CategoryService {
    CategoryResponse getCategory(UUID id);
    public PageResponse<CategoryResponse> getAllCategory(int page, int size, int limit) ;
    CategoryResponse saveCategory(CategoryRequest request);
    CategoryResponse updateCategory(UUID id, CategoryRequest request);
    void deleteCategory(UUID id);
    public CategoryResponse getCategoryById(UUID id);
}
