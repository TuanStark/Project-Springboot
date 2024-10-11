package com.stark.webbanhang.api.user.mapper;

import com.stark.webbanhang.api.user.dto.request.GalleryRequest;
import com.stark.webbanhang.api.user.dto.response.GalleryResponse;
import com.stark.webbanhang.api.user.entity.Gallery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GalleryMapper {
    Gallery toGallery(GalleryRequest request);
    GalleryResponse toGalleryResponse(Gallery gallery);
    void updateGallery(@MappingTarget Gallery gallery, GalleryRequest request);
}
