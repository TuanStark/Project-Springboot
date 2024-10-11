package com.stark.webbanhang.api.user.service.impl;

import com.stark.webbanhang.api.user.dto.response.CategoryResponse;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.entity.Category;
import com.stark.webbanhang.api.user.entity.Slide;
import com.stark.webbanhang.api.user.repository.SlideRepository;
import com.stark.webbanhang.helper.base.construct.RestfullService;
import com.stark.webbanhang.utils.uploads.UploadService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SlideService {
    private SlideRepository slideRepository;
    private UploadService uploadService;

    @Autowired
    public SlideService(SlideRepository slideRepository,UploadService uploadService) {
        this.slideRepository = slideRepository;
        this.uploadService = uploadService;
    }
    @Transactional
    public Slide store(MultipartFile file){
        boolean filePath = uploadService.saveFile(file);
        Slide slide = new Slide();
        if(filePath){
            slide.setImage(file.getOriginalFilename());
            slide.setCreatedAt(new Date());
        }
        return slideRepository.save(slide);
    }
    @Transactional
    public Slide update(UUID id, MultipartFile file){
        Slide slideExist = slideRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Not found Slide"));
        if (file != null && !file.isEmpty()){
            var filePath = uploadService.saveFile(file);
            slideExist.setImage(file.getOriginalFilename());
        }
        slideExist.setUpdatedAt(new Date());
        return slideRepository.save(slideExist);
    }

    @Transactional
    public void destroy(UUID id){
        Slide slideExist = slideRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Not found Slide"));
        String filePath = slideExist.getImage();

        if (uploadService.deleteFile(filePath)) {
            slideRepository.delete(slideExist);
        } else {
            throw new RuntimeException("Failed to delete the image file associated with the slide.");
        }
    }
    @Transactional
    public void delete(UUID id){
        Slide slideExist = slideRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Not found Slide"));
        slideRepository.delete(slideExist);
    }

    public PageResponse<Slide> getAll(int page, int size) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size,sort);

        Page<Slide> pageData = slideRepository.findAll(pageable);
        return PageResponse.<Slide>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPage(pageData.getTotalPages())
                .totalElement(pageData.getTotalElements())
                .data(pageData.getContent())
                .build();
    }
}
