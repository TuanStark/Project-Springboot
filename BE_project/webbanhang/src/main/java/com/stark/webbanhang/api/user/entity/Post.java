package com.stark.webbanhang.api.user.entity;

import com.stark.webbanhang.helper.base.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Post extends BaseEntity {
    private String title;
    private String description;
    private String alias;
    @Lob
    private String detail;
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
