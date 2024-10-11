package com.stark.webbanhang.api.user.entity;

import com.stark.webbanhang.helper.base.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "news")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class News extends BaseEntity {
    private String title;
    @Lob
    private String content;
    private String summary;
    private String image;
    private String alias;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
