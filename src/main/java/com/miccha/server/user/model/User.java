package com.miccha.server.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

@Getter
@Table("user")
@RequiredArgsConstructor
public class User {
    @Id
    private Long id;
    @NonNull
    private String email;
    @NonNull
    private String name;
    @NonNull
    private String password;
    private String token;
}
