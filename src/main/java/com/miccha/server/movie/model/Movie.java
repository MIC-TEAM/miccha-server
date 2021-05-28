package com.miccha.server.movie.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Map;

@Getter
@Setter
@Table
public class Movie {
    @Id
    private Long id;
    private String title;
    private String description;
    private String rating;
    private Integer duration;
    private String thumbnail;

    public static Movie of(Map<String, Object> columnMap) {
        Movie movie = new Movie();
        movie.setId((Long) columnMap.get("id"));
        movie.setTitle((String) columnMap.get("title"));
        movie.setDescription((String) columnMap.get("description"));
        movie.setRating((String) columnMap.get("rating"));
        movie.setDuration((Integer) columnMap.get("duration"));
        movie.setThumbnail((String) columnMap.get("thumbnail"));
        return movie;
    }
}
