package com.stark.webbanhang.api.admin.controller;

import com.stark.webbanhang.api.user.dto.request.CategoryRequest;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.helper.base.constant.StatusMessage;
import com.stark.webbanhang.helper.base.response.ResponseObject;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ui.Model;
import com.stark.webbanhang.api.user.dto.response.CategoryResponse;
import com.stark.webbanhang.api.user.service.CategoryService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

//@Controller
@RestController
@RequestMapping("/admin/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CategoryController {
    CategoryService categoryService;
    @GetMapping
    public String getAllCategory(Model model,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                 @RequestParam(value = "size", required = false, defaultValue = "6") int size,
                                 @RequestParam(value = "size", required = false) int limit) {
        PageResponse<CategoryResponse> response = categoryService.getAllCategory(page, size,limit);
        model.addAttribute("category", response.getData());
        model.addAttribute("currentPage", response.getCurrentPage());
        model.addAttribute("totalPages", response.getTotalPage());
        model.addAttribute("pageSize", response.getPageSize());
        model.addAttribute("totalElements", response.getTotalElement());

        return "admin/category/index";
    }
    @GetMapping("/add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("categoryRequest", new CategoryRequest());
        return "admin/category/add";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute("categoryRequest") CategoryRequest categoryRequest, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/category/add"; // Trả về form nếu có lỗi
        }
        // Thực hiện logic thêm mới danh mục
        categoryService.saveCategory(categoryRequest);
        return "redirect:/admin/category"; // Điều hướng về danh sách danh mục sau khi thêm
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable UUID id, Model model) {
        CategoryResponse category = categoryService.getCategory(id);
        model.addAttribute("category", category);
        return "admin/category/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable UUID id, @ModelAttribute CategoryRequest categoryRequest) {
        categoryService.updateCategory(id, categoryRequest);
        return "redirect:/admin/category";
    }

    @GetMapping("/remove/{id}")
    public String deleteCategory(@PathVariable UUID id){
        categoryService.deleteCategory(id);
        return "redirect:/admin/category";
    }
    /// API
    @GetMapping("/getAll")
    public ResponseObject<PageResponse> getAllCategory(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "limit", required = false) Integer limit) {
        if (size == null) {
            size = Integer.MAX_VALUE;
        }
        PageResponse<CategoryResponse> response = categoryService.getAllCategory(page, size,limit);
        return ResponseObject.<PageResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(response)
                .build();
    }

    @PostMapping("/create")
    public ResponseObject<CategoryResponse> create(@ModelAttribute CategoryRequest request){
        CategoryResponse response = categoryService.saveCategory(request);

        return ResponseObject.<CategoryResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(response)
                .build();
    }

    @PutMapping("/update/{id}")
    public ResponseObject<CategoryResponse> update(@Valid @PathVariable UUID id,
                                                   @ModelAttribute CategoryRequest request){
        CategoryResponse response = categoryService.updateCategory(id,request);

        return ResponseObject.<CategoryResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(response)
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseObject<Void> delete(@Valid @PathVariable UUID id){
        categoryService.deleteCategory(id);
        return ResponseObject.<Void>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .build();
    }

    @GetMapping("/getById/{id}")
    public ResponseObject<CategoryResponse> getById(@Valid @PathVariable UUID id){
        CategoryResponse response = categoryService.getCategoryById(id);
        return ResponseObject.<CategoryResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(response)
                .build();
    }
}
