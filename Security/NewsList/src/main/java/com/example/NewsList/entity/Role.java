package com.example.NewsList.entity;

import com.example.NewsList.enums.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private RoleType role;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username")
    private UserEntity user;

    public GrantedAuthority getAuthorities() {
        return new SimpleGrantedAuthority(role.name());
    }

    public static Role from (RoleType roleType) {
        Role role = new Role();

        role.setRole(roleType);

        return role;
    }

}
