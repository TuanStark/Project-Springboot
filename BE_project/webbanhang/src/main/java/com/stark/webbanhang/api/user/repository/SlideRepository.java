package com.stark.webbanhang.api.user.repository;

import com.stark.webbanhang.api.user.entity.Slide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SlideRepository extends JpaRepository<Slide, UUID> {
}
