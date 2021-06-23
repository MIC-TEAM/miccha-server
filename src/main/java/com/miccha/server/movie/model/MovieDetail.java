package com.miccha.server.movie.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MovieDetail {
    private List<String> director;
    private List<String> actor;
    private List<String> tags;
}

