package com.stark.webbanhang.api.admin.controller;

import com.stark.webbanhang.api.user.dto.request.NewsRequest;
import com.stark.webbanhang.api.user.dto.request.PostRequest;
import com.stark.webbanhang.api.user.dto.response.NewsResponse;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.dto.response.PostResponse;
import com.stark.webbanhang.api.user.service.NewsService;
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
@RequestMapping("/admin/news")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class NewsController {
    NewsService newsService;

    @PostMapping("/add")
    public ResponseObject<NewsResponse> create(@ModelAttribute NewsRequest request,
                                               @RequestParam MultipartFile file,
                                               @RequestHeader("Authorization") String authHeader){
        NewsResponse postResponse = newsService.createNews(request,file,authHeader);

        return ResponseObject.<NewsResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(postResponse)
                .build();
    }

    @PutMapping("/update/{id}")
    public ResponseObject<NewsResponse> update(@Valid @PathVariable UUID id,
                                               @ModelAttribute NewsRequest request,
                                               @RequestParam MultipartFile file,
                                               @RequestHeader("Authorization") String authHeader){
        NewsResponse newsResponse = newsService.updateNews(id,request,file,authHeader);

        return ResponseObject.<NewsResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(newsResponse)
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
        PageResponse<NewsResponse> response = newsService.getAllNews(page, size,authHeader,limit);
        return ResponseObject.<PageResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(response)
                .build();
    }

    @GetMapping("/getByID/{id}")
    public ResponseObject<NewsResponse> getById(
            @PathVariable UUID id,
            @RequestHeader("Authorization") String authHeader) {

        NewsResponse response = newsService.getById(id,authHeader);
        return ResponseObject.<NewsResponse>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(response)
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseObject<Void> delete(@Valid @PathVariable UUID id){
        newsService.deleteNews(id);
        return ResponseObject.<Void>builder()
                .code(200)
                .message(StatusMessage.SUCCESS)
                .build();
    }

}
