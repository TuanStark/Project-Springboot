package com.stark.webbanhang.api.user.mapper;

import com.stark.webbanhang.api.user.dto.request.NewsRequest;
import com.stark.webbanhang.api.user.dto.request.PostRequest;
import com.stark.webbanhang.api.user.dto.response.NewsResponse;
import com.stark.webbanhang.api.user.entity.News;
import com.stark.webbanhang.api.user.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    News toNews(NewsRequest request);
    NewsResponse toNewsResponse(News news);
    void  updateNews(@MappingTarget News news, NewsRequest request);
}
