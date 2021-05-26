package com.miccha.server.movie.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MovieCollection {
    private String theme;
    private List<Movie> movies;
}
