package com.stark.webbanhang.api.user.service;

import com.stark.webbanhang.api.user.dto.request.GalleryRequest;
import com.stark.webbanhang.api.user.dto.request.ProductRequest;
import com.stark.webbanhang.api.user.dto.response.GalleryResponse;
import com.stark.webbanhang.api.user.entity.Gallery;
import com.stark.webbanhang.api.user.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
@Service
public interface GalleryService {
    public List<GalleryResponse> saveGalleryImages(Product product, List<MultipartFile> images, List<Boolean> isDefaultList);
    public List<GalleryResponse> updateGalleryImages(UUID id,Product product, List<MultipartFile> images, List<Boolean> isDefaultList);
    public GalleryResponse updateImage(UUID idImages, GalleryRequest request);
    public List<GalleryResponse> getAllGallery();
}
