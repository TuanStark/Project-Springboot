package com.stark.webbanhang.api.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stark.webbanhang.helper.base.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "gallery")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gallery extends BaseEntity {
    private String image;
    @Column(name = "is_default")
    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
