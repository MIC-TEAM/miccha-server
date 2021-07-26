package com.miccha.server.movie;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.miccha.server.movie.model.Movie;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class WishRepository {
    private R2dbcEntityTemplate template;

    public Mono<Integer> insertWish(String email, int movieid) {
        final String query = "insert into movie_wish_map (user_id, movie_id) select id, ? from user where email = ?";
        return template.getDatabaseClient()
                       .sql(query)
                       .bind(0, movieid)
                       .bind(1, email)
                       .fetch()
                       .rowsUpdated();
    }

    public Mono<Integer> deleteWish(String email, int movieid) {
        final String query = "delete from movie_wish_map where movie_id = ? and user_id = (select id from `user` where email  = ?) ";
        return template.getDatabaseClient()
                       .sql(query)
                       .bind(0, movieid)
                       .bind(1, email)
                       .fetch()
                       .rowsUpdated();
    }

    public Mono<Long> countWish(String email, int movieid) {
        final String query = "select count(*) as c from movie_wish_map where movie_id = ? and user_id = (select id from `user` where email  = ?) ";
        return template.getDatabaseClient()
                       .sql(query)
                       .bind(0, movieid)
                       .bind(1, email)
                       .fetch()
                       .first()
                       .map(x -> (Long) x.get("c"));
    }

    public Flux<Movie> getWish(String email) {
        final String query = "select m.* from movie_wish_map mwm " +
        "inner join movie m ON m.id = mwm.movie_id " +
        "inner join `user` u on u.id = mwm.user_id " +
        "where u.email = ?";
        return template.getDatabaseClient()
                       .sql(query)
                       .bind(0, email)
                       .fetch()
                       .all()
                       .map(columnMap -> Movie.of(columnMap));
    }
}
