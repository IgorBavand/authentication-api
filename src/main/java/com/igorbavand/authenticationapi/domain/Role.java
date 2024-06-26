package com.igorbavand.authenticationapi.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.*;

@Entity
@Data
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
