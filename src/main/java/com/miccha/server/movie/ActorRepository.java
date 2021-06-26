package com.miccha.server.movie;

import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@Service
public class ActorRepository {
    private R2dbcEntityTemplate template;

    public Flux<String> getAllByMovieId(int movieId) {
        final String query = "select a.value from movie_actor_map as mam inner join actor as a where mam.movie_id = ? and mam.actor_id = a.id";
        return template.getDatabaseClient()
                       .sql(query)
                       .bind(0, movieId)
                       .fetch()
                       .all()
                       .map(columnMap -> (String) columnMap.get("value"));
    }
}
