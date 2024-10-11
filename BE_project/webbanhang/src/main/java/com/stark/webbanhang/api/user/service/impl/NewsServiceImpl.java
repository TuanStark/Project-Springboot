package com.stark.webbanhang.api.user.service.impl;

import com.stark.webbanhang.api.user.dto.request.NewsRequest;
import com.stark.webbanhang.api.user.dto.response.NewsResponse;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.dto.response.PostResponse;
import com.stark.webbanhang.api.user.entity.News;
import com.stark.webbanhang.api.user.entity.Post;
import com.stark.webbanhang.api.user.entity.User;
import com.stark.webbanhang.api.user.mapper.NewsMapper;
import com.stark.webbanhang.api.user.repository.NewsRepository;
import com.stark.webbanhang.api.user.repository.UserRepository;
import com.stark.webbanhang.api.user.service.NewsService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NewsServiceImpl implements NewsService {
    UserRepository userRepository;
    AuthenticationService authenticationService;
    UploadService uploadService;
    NewsRepository newsRepository;
    NewsMapper newsMapper;

    @Override
    public NewsResponse createNews(NewsRequest request, MultipartFile file, String authHeader) {
        String token = authHeader.substring(7); // Lấy token từ header (bỏ "Bearer ")
        UUID idUser = authenticationService.getCurrentUserId(token);
        News news = null;
        if(file != null){
            uploadService.saveFile(file);
            news = newsMapper.toNews(request);
            news.setImage(file.getOriginalFilename());
            news.setCreatedAt(new Date());
            news.setAlias(request.getTitle().toLowerCase().replaceAll("\\s+", "-"));
        }
        User user = userRepository.findById(idUser).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        news.setUser(user);
        NewsResponse response = newsMapper.toNewsResponse(newsRepository.save(news));
        response.setLastName(user.getLastName());
        response.setCreatedAt((news.getCreatedAt()));

        return response;
    }

    @Override
    public NewsResponse updateNews(UUID idNews, NewsRequest request, MultipartFile file, String authHeader) {
        NewsResponse response = null;
        if(file != null){
            uploadService.saveFile(file);
            String token = authHeader.substring(7); // Lấy token từ header (bỏ "Bearer ")
            UUID idUser = authenticationService.getCurrentUserId(token);
            System.out.println(idUser);
            News newsexist = newsRepository.findById(idNews).orElseThrow(()->new RuntimeException("Not found News"));
            newsMapper.updateNews(newsexist,request);
            newsexist.setCreatedAt(new Date());
            newsexist.setImage(file.getOriginalFilename());
            newsexist.setAlias(request.getTitle().toLowerCase().replaceAll("\\s+", "-"));
            User user = userRepository.findById(idUser).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
            newsexist.setUser(user);

            response = newsMapper.toNewsResponse(newsRepository.save(newsexist));
            response.setLastName(user.getLastName());
        }

        return response;
    }

    @Override
    public void deleteNews(UUID id) {
        News postExist = newsRepository.findById(id).orElseThrow(()->new RuntimeException("Not found News"));
        newsRepository.delete(postExist);
    }

    @Override
    public PageResponse<NewsResponse> getAllNews(int page, int size, String authHeader, int limit) {
        String token = authHeader.substring(7);
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<News> pageData = newsRepository.findAll(pageable);

        UUID idUser = authenticationService.getCurrentUserId(token);
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<News> listNews = pageData.getContent();
        if (limit > 0) {
            listNews = listNews.stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        return PageResponse.<NewsResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPage(pageData.getTotalPages())
                .totalElement(pageData.getTotalElements())
                .data(listNews.stream().map(news -> {
                    NewsResponse response = newsMapper.toNewsResponse(news);
                    response.setLastName(user.getLastName());
                    response.setAlias(news.getAlias());
                    response.setCreatedAt((news.getCreatedAt()));
                    return response;
                }).toList())
                .build();
    }

    @Override
    public NewsResponse getById(UUID id, String authHeader) {
        String token = authHeader.substring(7); // Lấy token từ header (bỏ "Bearer ")
        UUID idUser = authenticationService.getCurrentUserId(token);
        News newsExist = newsRepository.findById(id).orElseThrow(()->new RuntimeException("Not found Post"));
        NewsResponse response = newsMapper.toNewsResponse(newsExist);
        response.setAlias(newsExist.getTitle().toLowerCase().replaceAll("\\s+", "-"));
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        response.setLastName(user.getLastName());
        return response;
    }
}
