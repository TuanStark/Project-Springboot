package com.stark.webbanhang.api.user.service.impl;

import com.stark.webbanhang.api.user.dto.request.PostRequest;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.dto.response.PostResponse;
import com.stark.webbanhang.api.user.entity.Post;
import com.stark.webbanhang.api.user.entity.User;
import com.stark.webbanhang.api.user.mapper.PostMapper;
import com.stark.webbanhang.api.user.repository.PostRepository;
import com.stark.webbanhang.api.user.repository.UserRepository;
import com.stark.webbanhang.api.user.service.PostService;
import com.stark.webbanhang.helper.exception.AppException;
import com.stark.webbanhang.helper.exception.ErrorCode;
import com.stark.webbanhang.utils.uploads.UploadService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    UserRepository userRepository;
    AuthenticationService authenticationService;
    UploadService uploadService;

    @Override
    @Transactional
    public PostResponse createPost(PostRequest request, MultipartFile file,String authHeader) {
        String token = authHeader.substring(7); // Lấy token từ header (bỏ "Bearer ")
        UUID idUser = authenticationService.getCurrentUserId(token);
        System.out.println(idUser);
        Post post = null;
        if(file != null){
            uploadService.saveFile(file);
            post = postMapper.toPost(request);
            post.setImage(file.getOriginalFilename());
            post.setCreatedAt(new Date());
            post.setAlias(request.getTitle().toLowerCase().replaceAll("\\s+", "-"));
        }
        User user = userRepository.findById(idUser).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        post.setUser(user);
        PostResponse response = postMapper.toPostresponse(postRepository.save(post));
        response.setLastName(user.getLastName());
        response.setCreatedAt((post.getCreatedAt()));

        return response;
    }

    @Override
    @Transactional
    public PostResponse updatePost(UUID id, PostRequest request, MultipartFile file,String authHeader) {
        PostResponse response = null;
        if(file != null){
            uploadService.saveFile(file);
            String token = authHeader.substring(7); // Lấy token từ header (bỏ "Bearer ")
            UUID idUser = authenticationService.getCurrentUserId(token);
            System.out.println(idUser);
            Post postExist = postRepository.findById(id).orElseThrow(()->new RuntimeException("Not found Post"));
            postMapper.updatePost(postExist,request);
            postExist.setCreatedAt(new Date());
            postExist.setImage(file.getOriginalFilename());
            postExist.setAlias(request.getTitle().toLowerCase().replaceAll("\\s+", "-"));
            User user = userRepository.findById(idUser).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
            postExist.setUser(user);

            response = postMapper.toPostresponse(postRepository.save(postExist));
            response.setLastName(user.getLastName());
        }

        return response;
    }

    @Override
    @Transactional
    public void deletePost(UUID id) {
        Post postExist = postRepository.findById(id).orElseThrow(()->new RuntimeException("Not found Post"));
        postRepository.delete(postExist);
    }

    @Override
    public PageResponse<PostResponse> getAllPost(int page, int size,String authHeader, int limit) {
        String token = authHeader.substring(7);
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Post> pageData = postRepository.findAll(pageable);

        UUID idUser = authenticationService.getCurrentUserId(token);
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Post> listPost = pageData.getContent();
        if (limit > 0) {
            listPost = listPost.stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPage(pageData.getTotalPages())
                .totalElement(pageData.getTotalElements())
                .data(listPost.stream().map(post -> {
                    PostResponse response = postMapper.toPostresponse(post);
                    response.setLastName(user.getLastName());
                    response.setAlias(post.getAlias());
                    response.setCreatedAt((post.getCreatedAt()));
                    return response;
                }).toList())
                .build();
    }


    @Override
    public PostResponse getById(UUID id,String authHeader) {
        String token = authHeader.substring(7); // Lấy token từ header (bỏ "Bearer ")
        UUID idUser = authenticationService.getCurrentUserId(token);
        Post postExist = postRepository.findById(id).orElseThrow(()->new RuntimeException("Not found Post"));
        PostResponse response = postMapper.toPostresponse(postExist);
        response.setAlias(postExist.getTitle().toLowerCase().replaceAll("\\s+", "-"));
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        response.setLastName(user.getLastName());
        return response;
    }
}
