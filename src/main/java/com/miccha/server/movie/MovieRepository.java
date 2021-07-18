package com.miccha.server.movie;

import com.miccha.server.movie.model.Movie;
import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;

@AllArgsConstructor
@Service
public class MovieRepository {
    private R2dbcEntityTemplate template;

    public Mono<Movie> get(int movieId) {
        return template.select(Movie.class)
                       .matching(Query.query(where("id").is(movieId)))
                       .first();
    }

    public Flux<Movie> getByTagId(@NonNull Long category) {
        final String query = "select m.* from movie as m inner join movie_tag_map as mtm where m.id = mtm.movie_id and mtm.tag_id = ?";
        return template.getDatabaseClient()
                       .sql(query)
                       .bind(0, category)
                       .fetch()
                       .all()
                       .map(columnMap -> Movie.of(columnMap));
    }

    public Flux<Movie> getByKeyword(@NonNull String keyword) {
        final String query = "select m.* from keyword_movie_map as kmm inner join movie as m where kmm.keyword LIKE concat(?, '%') AND kmm.movie_id = m.id";
        return template.getDatabaseClient()
                       .sql(query)
                       .bind(0, keyword)
                       .fetch()
                       .all()
                       .map(columnMap -> Movie.of(columnMap));
    }
}
