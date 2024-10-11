package com.stark.webbanhang.api.user.repository;

import com.stark.webbanhang.api.user.entity.Category;
import com.stark.webbanhang.api.user.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCategory(Category category);
    List<Product> findByCategoryId(UUID id);
    Page<Product> findByCategoryId(UUID categoryId, Pageable pageable);
}
