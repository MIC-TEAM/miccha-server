package com.miccha.server.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("user")
@RequiredArgsConstructor
public class User {
    @Id
    private Long id;
    private String email;
    private String name;
    private String password;
    private String token;
}
