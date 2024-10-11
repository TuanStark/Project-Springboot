package com.stark.webbanhang.api.user.repository;

import com.stark.webbanhang.api.user.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
}
