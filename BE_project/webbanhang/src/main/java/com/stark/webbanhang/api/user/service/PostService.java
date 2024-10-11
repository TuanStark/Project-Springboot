package com.stark.webbanhang.api.user.service;

import com.stark.webbanhang.api.user.dto.request.PostRequest;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.dto.response.PostResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public interface PostService {
     PostResponse createPost(PostRequest request, MultipartFile file,String authHeader);
     PostResponse updatePost(UUID id, PostRequest request, MultipartFile file,String authHeader);
     void deletePost(UUID id);
     PageResponse<PostResponse> getAllPost(int page, int size, String authHeader, int limit);
     PostResponse getById(UUID id,String authHeader);
}
