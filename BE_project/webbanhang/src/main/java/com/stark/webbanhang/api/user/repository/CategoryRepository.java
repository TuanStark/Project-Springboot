package com.stark.webbanhang.api.user.repository;

import com.stark.webbanhang.api.user.entity.Category;
import com.stark.webbanhang.api.user.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Category findByName(String name);
}
