package com.miccha.server.movie.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table
public class Tag {
    @Id
    private Long id;
    private String type;
    private String value;
}
