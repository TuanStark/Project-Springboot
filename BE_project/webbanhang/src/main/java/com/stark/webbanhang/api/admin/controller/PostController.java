package com.stark.webbanhang.api.admin.controller;

import com.stark.webbanhang.api.user.dto.request.PostRequest;
import com.stark.webbanhang.api.user.dto.response.CategoryResponse;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.dto.response.PostResponse;
import com.stark.webbanhang.api.user.service.PostService;
import com.stark.webbanhang.helper.base.constant.StatusMessage;
import com.stark.webbanhang.helper.base.response.ResponseObject;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/admin/post")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping("/add")
    public ResponseObject<PostResponse> create(@ModelAttribute PostRequest request,
                                               @RequestParam MultipartFile file,
                                               @RequestHeader("Authorization") String authHeader){
        PostResponse postResponse = postService.createPost(request,file,authHeader);

        return ResponseObject.<PostResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(postResponse)
                .build();
    }

    @PutMapping("/update/{id}")
    public ResponseObject<PostResponse> update(@Valid @PathVariable UUID id,
                                               @ModelAttribute PostRequest request,
                                               @RequestParam MultipartFile file,
                                               @RequestHeader("Authorization") String authHeader){
        PostResponse postResponse = postService.updatePost(id,request,file,authHeader);

        return ResponseObject.<PostResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(postResponse)
                .build();
    }


    @GetMapping("/getAll")
    public ResponseObject<PageResponse> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "limit", required = false) Integer limit) {
        if (size == null) {
            size = Integer.MAX_VALUE;
        }
        PageResponse<PostResponse> response = postService.getAllPost(page, size,authHeader,limit);
        return ResponseObject.<PageResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(response)
                .build();
    }
    @GetMapping("/getByID/{id}")
    public ResponseObject<PostResponse> getById(
            @PathVariable UUID id,
            @RequestHeader("Authorization") String authHeader) {

        PostResponse response = postService.getById(id,authHeader);
        return ResponseObject.<PostResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(response)
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseObject<Void> delete(@Valid @PathVariable UUID id){
        postService.deletePost(id);
        return ResponseObject.<Void>builder()
                .code(200)
                .message(StatusMessage.SUCCESS)
                .build();
    }
}
