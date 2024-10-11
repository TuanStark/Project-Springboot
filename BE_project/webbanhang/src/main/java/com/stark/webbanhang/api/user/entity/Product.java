package com.stark.webbanhang.api.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stark.webbanhang.helper.base.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {
    private String name;
    private String description;
    private double price;
    private int discount;
    private String image;// cai ni chua biet co nen co khong
    private String ingredient;
    private String size;
    private int weight;
    private int quantity;
    private int status;
    private String note;
    @Column(columnDefinition = "TEXT")
    private String content;
    private boolean hot;
    @Column(name = "new_product")
    private boolean newProduct;
    @Column(name = "promotion_product")
    private boolean promotionProduct;
    private String alias;

    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH,
            CascadeType.DETACH
    })
    private Set<Comment> comment = new HashSet<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Gallery> gallery = new HashSet<>();
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetail = new HashSet<>();
}
