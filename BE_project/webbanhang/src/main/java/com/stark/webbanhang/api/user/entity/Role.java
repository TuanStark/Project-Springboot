package com.stark.webbanhang.api.user.entity;


import com.stark.webbanhang.helper.base.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;



import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity{
    private String name;
    private String description;
    @OneToMany(mappedBy = "role",cascade = CascadeType.ALL)
    private Set<User> user = new HashSet<>();
}
