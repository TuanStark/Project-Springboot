package com.stark.webbanhang.api.user.mapper;

import com.stark.webbanhang.api.user.dto.request.PostRequest;
import com.stark.webbanhang.api.user.dto.response.PostResponse;
import com.stark.webbanhang.api.user.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostRequest request);
    PostResponse toPostresponse(Post post);
    void updatePost(@MappingTarget Post post, PostRequest request);
}
