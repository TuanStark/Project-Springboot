package com.stark.webbanhang.api.user.repository;

import com.stark.webbanhang.api.user.dto.response.GalleryResponse;
import com.stark.webbanhang.api.user.entity.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GalleryRepository extends JpaRepository<Gallery, UUID> {
    // Tìm tất cả các ảnh của sản phẩm có isDefault = true
    List<Gallery> findByProduct_IdAndIsDefaultTrue(UUID productId);
    List<Gallery> findByProduct_Id(UUID id);

    @Query("SELECT i FROM Gallery i WHERE i.isDefault = true AND i.product.id = :productId")
    Gallery findDefaultImagesByProductId(@Param("productId") UUID productId);

    //@Query("SELECT i FROM Gallery i WHERE i.isDefault = false AND i.product.id = :productId")
    //List<Gallery> findDefaultFalseImagesByProductId(@Param("productId") UUID productId);

    @Query("SELECT new com.stark.webbanhang.api.user.dto.response.GalleryResponse(i.id, i.isDefault, i.image) FROM Gallery i WHERE i.product.id = :productId")
    List<GalleryResponse> findImagesAndDefaultsByProductId(@Param("productId") UUID productId);
}
