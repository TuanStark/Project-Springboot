package com.stark.webbanhang.api.admin.controller;


import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.entity.Slide;
import com.stark.webbanhang.api.user.service.impl.SlideService;
import com.stark.webbanhang.helper.base.constant.StatusMessage;
import com.stark.webbanhang.helper.base.response.ResponseObject;
import com.stark.webbanhang.utils.uploads.UploadService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/admin/slide")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class SlideController {
    SlideService slideService;

    @PostMapping("/upload")
    public ResponseObject<Slide> create(@RequestParam MultipartFile file){
        Slide slide = slideService.store(file);
        return ResponseObject.<Slide>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(slide)
                .build();
    }

    @GetMapping("/all")// cái này chưa đủ
    public ResponseObject<PageResponse<Slide>> getAll(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                              @RequestParam(value = "size", required = false, defaultValue = "6") Integer size){
        PageResponse<Slide> list = slideService.getAll(page,size);
        return ResponseObject.<PageResponse<Slide>>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(list)
                .build();
    }

    @PutMapping("/update/{id}")
    public ResponseObject<Slide> update(@Valid @PathVariable UUID id, @RequestParam MultipartFile file){
        Slide slide = slideService.update(id,file);
        return ResponseObject.<Slide>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .data(slide)
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseObject<Void> delete(@Valid @PathVariable UUID id){
        slideService.delete(id);
        return ResponseObject.<Void>builder()
                .message(StatusMessage.SUCCESS)
                .code(200)
                .build();
    }

}
