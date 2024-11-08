package com.stark.webbanhang.api.admin.controller;

import com.stark.webbanhang.api.user.dto.request.ProductRequest;
import com.stark.webbanhang.api.user.dto.response.CategoryResponse;
import com.stark.webbanhang.api.user.dto.response.GalleryResponse;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.dto.response.ProductResponse;
import com.stark.webbanhang.api.user.service.GalleryService;
import com.stark.webbanhang.api.user.service.ProductService;
import com.stark.webbanhang.helper.base.constant.StatusMessage;
import com.stark.webbanhang.helper.base.response.ResponseObject;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/admin/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProductController {
    ProductService productService;
    GalleryService galleryService;

    @PostMapping("/add")
    public ResponseObject<ProductResponse> create(@ModelAttribute ProductRequest request){
        ProductResponse response = productService.createProduct(request);
        return ResponseObject.<ProductResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(response)
                .build();
    }

    @PutMapping("/update/{id}")
    public ResponseObject<ProductResponse> update(@Valid @PathVariable UUID id, @ModelAttribute ProductRequest request){
        ProductResponse response = productService.updateProduct(id,request);
        return ResponseObject.<ProductResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(response)
                .build();
    }

    @GetMapping("/getAll")
    public ResponseObject<PageResponse> getAllProduct(
                                 @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                 @RequestParam(value = "size", required = false) Integer size) {
        if (size == null) {
            size = Integer.MAX_VALUE;
        }
        PageResponse<ProductResponse> response = productService.getAllProduct(page, size);
        return ResponseObject.<PageResponse>builder()
                .data(response)
                .message(StatusMessage.SUCCESS)
                .code(200)
                .build();
    }

    @DeleteMapping("/delete/{idProduct}")
    String delete(@PathVariable UUID idProduct){
        productService.deleteProduct(idProduct);
        return "Product is deleted";
    }

    @GetMapping("/getById/{id}")
    public ResponseObject<ProductResponse> getById(@Valid @PathVariable UUID id){
        ProductResponse response = productService.getProductById(id);
        return ResponseObject.<ProductResponse>builder()
                .code(200)
                .message(StatusMessage.SUCCESS)
                .data(response)
                .build();
    }

    @GetMapping("/getAllGallery")
    public ResponseObject<List<GalleryResponse>> getAllGallery(){
        List<GalleryResponse> list = galleryService.getAllGallery();

        return ResponseObject.<List<GalleryResponse>>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(list)
                .build();
    }

    @GetMapping("/getByCategory/{categoryId}")
    public ResponseObject<PageResponse> getProductByIdCategoryId(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false) Integer size,
            @PathVariable UUID categoryId){
        if (size == null) {
            size = Integer.MAX_VALUE;
        }
        PageResponse<ProductResponse> result = productService.getProductByIdCategory(page,size,categoryId);
        return ResponseObject.<PageResponse>builder()
                .code(200)
                .message(StatusMessage.SUCCESS)
                .data(result)
                .build();
    }
}
