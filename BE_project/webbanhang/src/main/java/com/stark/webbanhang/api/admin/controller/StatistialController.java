package com.stark.webbanhang.api.admin.controller;

import com.stark.webbanhang.api.user.service.impl.StatisticalService;
import com.stark.webbanhang.helper.base.constant.StatusMessage;
import com.stark.webbanhang.helper.base.response.ResponseObject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/statistics")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class StatistialController {
    StatisticalService statisticalService;

    @GetMapping("/monthly-earnings")
    public ResponseObject<Map<String, Object>> getMonthlyEarnings(@RequestParam int year) {
        Map<String, Object> earningsData = statisticalService.getMonthlyEarnings(year);
        return ResponseObject.<Map<String, Object>>builder()
                .data(earningsData)
                .code(200)
                .message(StatusMessage.SUCCESS)
                .build();
    }
}
