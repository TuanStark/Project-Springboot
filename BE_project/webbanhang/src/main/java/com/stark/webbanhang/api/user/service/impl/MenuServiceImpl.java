package com.stark.webbanhang.api.user.service.impl;

import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.entity.Menu;
import com.stark.webbanhang.api.user.repository.MenuRepository;
import com.stark.webbanhang.api.user.service.MenuService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
@Service
@Slf4j
@RequiredArgsConstructor// thay co contructer nó sẽ tự động inject
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuServiceImpl implements MenuService {
    MenuRepository menuRepository;

    @Override
    public Menu createMenu(Menu request) {
        Menu menu = new Menu();
        menu.setName(request.getName());
        menu.setLink(request.getLink());
        menu.setCreatedAt(new Date());
        return null;
    }

    @Override
    public Menu updateMenu(UUID id, Menu menu) {
        Menu existMenu = menuRepository.findById(id).orElseThrow(()-> new RuntimeException("Not found Menu"));

        return null;
    }

    @Override
    public PageResponse<Menu> getAllMenu(int page, int size) {
        return null;
    }

    @Override
    public void deleteMenu(UUID id) {

    }

    @Override
    public Menu getById(UUID id) {
        return null;
    }
}
