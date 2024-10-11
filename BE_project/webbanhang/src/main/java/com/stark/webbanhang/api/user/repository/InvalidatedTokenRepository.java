package com.stark.webbanhang.api.user.repository;

import com.stark.webbanhang.api.user.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, UUID> {
}
