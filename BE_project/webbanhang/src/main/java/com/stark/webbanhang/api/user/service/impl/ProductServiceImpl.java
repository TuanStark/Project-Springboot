package com.stark.webbanhang.api.user.service.impl;

import com.stark.webbanhang.api.user.dto.request.ProductRequest;
import com.stark.webbanhang.api.user.dto.response.CategoryResponse;
import com.stark.webbanhang.api.user.dto.response.GalleryResponse;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.dto.response.ProductResponse;
import com.stark.webbanhang.api.user.entity.Category;
import com.stark.webbanhang.api.user.entity.Gallery;
import com.stark.webbanhang.api.user.entity.Product;
import com.stark.webbanhang.api.user.mapper.CategoryMapper;
import com.stark.webbanhang.api.user.mapper.ProductMapper;
import com.stark.webbanhang.api.user.repository.CategoryRepository;
import com.stark.webbanhang.api.user.repository.GalleryRepository;
import com.stark.webbanhang.api.user.repository.ProductRepository;
import com.stark.webbanhang.api.user.service.GalleryService;
import com.stark.webbanhang.api.user.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
     ProductRepository productRepository;
     GalleryService galleryService;
     GalleryRepository galleryRepository;
     ProductMapper productMapper;
     CategoryRepository categoryRepository;
     CategoryMapper categoryMapper;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = productMapper.toProduct(request);
        product.setAlias(request.getName().toLowerCase().replaceAll("\\s+", "-"));
        Category category = categoryRepository.findById(request.getCategoryID())
                .orElseThrow(()-> new RuntimeException("Not found Category"));
        product.setCategory(category);
        productRepository.save(product);
        // Upload ảnh liên quan tới sản phẩm
        galleryService.saveGalleryImages(product,request.getImages(),request.getIsDefaultList());
        ProductResponse response = this.convertToResponse(product);
        return response;
    }

    @Override
    public ProductResponse updateProduct(UUID id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Not found Product"));
        product.setAlias(request.getName().toLowerCase().replaceAll("\\s+", "-"));
        productMapper.updateProduct(product,request);
        Category category = categoryRepository.findById(request.getCategoryID())
                .orElseThrow(()-> new RuntimeException("Not found Category"));
        product.setCategory(category);
        productRepository.save(product);

        // Upload ảnh liên quan tới sản phẩm
        galleryService.updateGalleryImages(product.getId(),product,request.getImages(),request.getIsDefaultList());
        ProductResponse response = this.convertToResponse(product);
        return response;
    }

    @Override
    public PageResponse<ProductResponse> getAllProduct(int page, int size) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page-1,size,sort);

        Page<Product> pageData = productRepository.findAll(pageable);

        return PageResponse.<ProductResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPage(pageData.getTotalPages())
                .totalElement(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(this::convertToResponse).toList())
                .build();
    }

    public ProductResponse convertToResponse(Product product) {
        ProductResponse response = productMapper.toProductResponse(product);
        response.setCategory(categoryMapper.toCategoryResponse(product.getCategory()));
        // Lấy hình ảnh mặc định của sản phẩm
        List<GalleryResponse> list = galleryRepository.findImagesAndDefaultsByProductId(product.getId());
        response.setImages(list);
        Gallery gallery = galleryRepository.findDefaultImagesByProductId(product.getId());
        System.out.println(gallery.getImage());
        response.setImage(gallery.getImage());
        return response;
    }


    @Override
    public void deleteProduct(UUID id) {
        List<Gallery> list = galleryRepository.findByProduct_Id(id);
        galleryRepository.deleteAll(list);
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Not found Product"));
        return convertToResponse(product);
    }

    @Override
    public PageResponse<ProductResponse> getProductByIdCategory(int page, int size, UUID categoryId) {
        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(page-1,size,sort);
        Page<Product> pageData = productRepository.findByCategoryId(categoryId,pageable);
        List<Product> listCategory = pageData.getContent();

        return PageResponse.<ProductResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPage(pageData.getTotalPages())
                .totalElement(pageData.getTotalElements())
                .data(listCategory.stream().map(this::convertToResponse).toList())
                .build();
    }
}
