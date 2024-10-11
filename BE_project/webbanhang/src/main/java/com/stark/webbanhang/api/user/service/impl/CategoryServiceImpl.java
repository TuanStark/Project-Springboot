package com.stark.webbanhang.api.user.service.impl;

import com.stark.webbanhang.api.user.dto.request.CategoryRequest;
import com.stark.webbanhang.api.user.dto.response.CategoryResponse;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.entity.Category;
import com.stark.webbanhang.api.user.entity.Product;
import com.stark.webbanhang.api.user.mapper.CategoryMapper;
import com.stark.webbanhang.api.user.repository.CategoryRepository;
import com.stark.webbanhang.api.user.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor// thay co contructer nó sẽ tự động inject
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryMapper categoryMapper;
    CategoryRepository categoryRepository;


    @Override
    public CategoryResponse getCategory(UUID id) {
        return categoryMapper.toCategoryResponse(categoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Not found Category")));
    }

    @Override
    public PageResponse<CategoryResponse> getAllCategory(int page, int size, int limit) {
        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(page-1,size,sort);
        Page<Category> pageData = categoryRepository.findAll(pageable);
        List<Category> listCategory = pageData.getContent();
        if (limit > 0) {
            listCategory = listCategory.stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        }
        return PageResponse.<CategoryResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPage(pageData.getTotalPages())
                .totalElement(pageData.getTotalElements())
                .data(listCategory.stream().map(categoryMapper::toCategoryResponse).toList())
                .build();
    }

    @Override
    @Transactional
    public CategoryResponse saveCategory(CategoryRequest request) {
        Category category = categoryMapper.toCategory(request);
        category.setCreatedAt(new Date());
        category.setAlias(request.getName().toLowerCase().replaceAll("\\s+", "-"));// Sử dụng tên từ request làm alias
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(UUID id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Not Found Category!"));
        category.setAlias(request.getName().toLowerCase().replaceAll("\\s+", "-"));// Sử dụng tên từ request làm alias
        category.setUpdatedAt(new Date());
        categoryMapper.updateCategory(category,request);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Not found category"));
        for(Product product : category.getProduct()) {
            product.setCategory(null);
        }
        category.getProduct().clear(); // Clear the collection
        categoryRepository.delete(category);
    }

    @Override
    public CategoryResponse getCategoryById(UUID id) {
        Category category = categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("Not found Category"));
        return categoryMapper.toCategoryResponse(category);
    }

}
