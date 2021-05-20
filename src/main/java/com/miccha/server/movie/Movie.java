package com.miccha.server.movie;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

@Data
@RequiredArgsConstructor
@Table("movie")
public class Movie {
    @Id
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private String rating;
    @NonNull
    private Long duration;
    @NonNull
    private String thumbnail;
}
