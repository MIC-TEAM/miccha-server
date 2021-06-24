package com.miccha.server.movie;

import com.miccha.server.movie.model.Movie;
import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@Service
public class MovieRepository {
    private R2dbcEntityTemplate template;

    public Flux<Movie> getByTagId(@NonNull Long category) {
        final String query = "select m.* from movie as m inner join movie_tag_map as mtm where m.id = mtm.movie_id and mtm.tag_id = ?";
        return template.getDatabaseClient()
                       .sql(query)
                       .bind(0, category)
                       .fetch()
                       .all()
                       .map(columnMap -> Movie.of(columnMap));
    }
}
