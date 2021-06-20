package com.miccha.server.movie;

import com.miccha.server.movie.model.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.data.relational.core.query.Criteria.where;

@AllArgsConstructor
@Service
public class TagRepository {
    private R2dbcEntityTemplate template;

    public Flux<Tag> getWithBounds(int offset, int limit) {
        return template.select(Tag.class)
                       .matching(Query.query(where("id").greaterThanOrEquals(offset)).limit(limit))
                       .all();
    }

    public Flux<Tag> getAllTags() {
        return template.select(Tag.class)
                       .all();
    }
}
