package com.stark.webbanhang.api.user.service;

import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.entity.Menu;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface MenuService {
    public Menu createMenu(Menu request);

    public Menu updateMenu(UUID id, Menu menu);

    public PageResponse<Menu> getAllMenu(int page, int size);

    public void deleteMenu(UUID id);

    public Menu getById(UUID id);
}