package com.stark.webbanhang.api.user.service.impl;

import com.stark.webbanhang.api.user.dto.request.GalleryRequest;
import com.stark.webbanhang.api.user.dto.request.ProductRequest;
import com.stark.webbanhang.api.user.dto.response.GalleryResponse;
import com.stark.webbanhang.api.user.entity.Category;
import com.stark.webbanhang.api.user.entity.Gallery;
import com.stark.webbanhang.api.user.entity.Product;
import com.stark.webbanhang.api.user.mapper.GalleryMapper;
import com.stark.webbanhang.api.user.mapper.ProductMapper;
import com.stark.webbanhang.api.user.repository.GalleryRepository;
import com.stark.webbanhang.api.user.repository.ProductRepository;
import com.stark.webbanhang.api.user.service.GalleryService;
import com.stark.webbanhang.utils.uploads.UploadService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GalleryServiceImpl implements GalleryService {
    GalleryRepository galleryRepository;
    GalleryMapper galleryMapper;
    UploadService uploadService;

    @Override
    @Transactional
    public List<GalleryResponse> saveGalleryImages(Product product, List<MultipartFile> images, List<Boolean> isDefaultList) {
        List<Gallery> listGallery = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            boolean fileSaved = uploadService.saveFile(images.get(i));
            boolean isDefault = isDefaultList.get(i);
            if (fileSaved) {
                Gallery gallery = new Gallery();
                gallery.setImage(images.get(i).getOriginalFilename());
                gallery.setDefault(isDefault);
                gallery.setProduct(product);
                gallery.setCreatedAt(new Date());
                if (isDefault == true) {
                    updateDefaultImage(product.getId());
                }
                listGallery.add(gallery);
            }
        }
        List<Gallery> savedGalleries = galleryRepository.saveAll(listGallery);
        List<GalleryResponse> listGalleryResponse = savedGalleries.stream()
                .map(galleryMapper::toGalleryResponse)
                .collect(Collectors.toList());
        return listGalleryResponse;
    }

    @Override
    public List<GalleryResponse> updateGalleryImages(UUID id, Product product, List<MultipartFile> images, List<Boolean> isDefaultList) {
        if (images != null && !images.isEmpty()) {
            List<Gallery> list = galleryRepository.findByProduct_Id(id);
            galleryRepository.deleteAll(list);
            List<Gallery> listGallery = new ArrayList<>();
            for (int i = 0; i < images.size(); i++) {
                boolean fileSaved = uploadService.saveFile(images.get(i));
                boolean isDefault = isDefaultList.get(i);
                if (fileSaved) {
                    Gallery gallery = new Gallery();
                    gallery.setImage(images.get(i).getOriginalFilename());
                    gallery.setDefault(isDefault);
                    gallery.setProduct(product);
                    gallery.setCreatedAt(new Date());

                    if (isDefault) {
                        updateDefaultImage(product.getId());
                    }

                    listGallery.add(gallery);
                }
            }
            List<Gallery> savedGalleries = galleryRepository.saveAll(listGallery);
            return savedGalleries.stream()
                    .map(galleryMapper::toGalleryResponse)
                    .collect(Collectors.toList());
        } else {
            List<Gallery> currentGalleries = galleryRepository.findByProduct_Id(id);
            return currentGalleries.stream()
                    .map(galleryMapper::toGalleryResponse)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public GalleryResponse updateImage(UUID idImages, GalleryRequest request) {
        Gallery galleryExist = galleryRepository.findById(idImages).orElseThrow(()-> new RuntimeException("Not found Images"));
        galleryExist.setUpdatedAt(new Date());
        galleryMapper.updateGallery(galleryExist,request);
        return galleryMapper.toGalleryResponse(galleryRepository.save(galleryExist));
    }

    @Override
    public List<GalleryResponse> getAllGallery() {
        List<Gallery> gallery = galleryRepository.findAll();
        return gallery.stream()
                .map(galleryMapper::toGalleryResponse)
                .collect(Collectors.toList());
    }

    public void updateDefaultImage(UUID productId) {
        List<Gallery> existingGalleries = galleryRepository.findByProduct_IdAndIsDefaultTrue(productId);
        for (Gallery gallery : existingGalleries) {
            gallery.setDefault(false);
            galleryRepository.save(gallery);
        }
    }
}
