package com.stark.webbanhang.api.user.service;

import com.stark.webbanhang.api.user.dto.request.NewsRequest;
import com.stark.webbanhang.api.user.dto.request.PostRequest;
import com.stark.webbanhang.api.user.dto.response.NewsResponse;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.dto.response.PostResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public interface NewsService {
    NewsResponse createNews(NewsRequest request, MultipartFile file, String authHeader);
    NewsResponse updateNews(UUID idNews,NewsRequest request, MultipartFile file,String authHeader);
    void deleteNews(UUID id);
    PageResponse<NewsResponse> getAllNews(int page, int size, String authHeader, int limit);
    NewsResponse getById(UUID id,String authHeader);
}
