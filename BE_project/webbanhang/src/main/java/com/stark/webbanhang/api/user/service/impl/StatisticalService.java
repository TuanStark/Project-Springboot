package com.stark.webbanhang.api.user.service.impl;

import com.stark.webbanhang.api.user.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticalService {
    OrderRepository orderRepository;

    public Map<String, Object> getMonthlyEarnings(int year) {
        List<Object[]> rawData = orderRepository.findMonthlyEarnings(year);

        // Khởi tạo dữ liệu với 12 tháng, giá trị mặc định là 0
        double[] earnings = new double[12];
        for (Object[] row : rawData) {
            int month = (int) row[0];
            double totalEarnings = (Double) row[1]; // Chuyển đổi sang Double
            earnings[month - 1] = totalEarnings;
        }

        // Chuẩn bị dữ liệu để trả về cho frontend
        Map<String, Object> response = new HashMap<>();
        response.put("labels", new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        response.put("earnings", earnings);

        return response;
    }
}
