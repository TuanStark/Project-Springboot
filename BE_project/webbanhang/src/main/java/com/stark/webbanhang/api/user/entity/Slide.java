package com.stark.webbanhang.api.user.entity;

import com.stark.webbanhang.helper.base.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "slides")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Slide extends BaseEntity {
    private String image;
}
