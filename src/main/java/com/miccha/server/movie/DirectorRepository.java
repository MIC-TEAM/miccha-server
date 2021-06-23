package com.miccha.server.movie;

import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@Service
public class DirectorRepository {
    private R2dbcEntityTemplate template;

    public Flux<String> getAllByMovieId(int movieId) {
        final String query = "select d.value from movie_director_map as mdm inner join director as d where mdm.movie_id = ? and mdm.director_id = d.id";
        return template.getDatabaseClient()
                       .sql(query)
                       .bind(0, movieId)
                       .fetch()
                       .all()
                       .map(columnMap -> (String) columnMap.get("value"));
    }
}
