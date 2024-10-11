package com.stark.webbanhang.api.user.repository;

import com.stark.webbanhang.api.user.entity.Menu;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, UUID> {
}
