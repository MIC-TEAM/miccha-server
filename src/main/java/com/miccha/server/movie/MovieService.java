package com.miccha.server.movie;

import com.miccha.server.config.Config;
import com.miccha.server.movie.model.MovieCollection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@AllArgsConstructor
@Service
public class MovieService {
    private Config config;
    private MovieRepository movieRepository;
    private TagRepository tagRepository;

    public Mono<List<MovieCollection>> getPage(int pageOffset) {
        List<MovieCollection> page = new CopyOnWriteArrayList<>();
        return tagRepository.getWithBounds(pageOffset * config.getPageSize(), config.getPageSize())
                            .flatMap(tag -> movieRepository.getByTagId(tag.getId())
                                                           .collectList()
                                                           .single()
                                                           .doOnSuccess(movies -> page.add(new MovieCollection(tag.getValue(), movies))))
                            .then(Mono.just(page));
    }

    public Mono<List<MovieCollection>> getCategories(int pageOffset) {
        List<MovieCollection> page = new CopyOnWriteArrayList<>();
        return tagRepository.getWithBounds(pageOffset * config.getPageSize(), config.getPageSize())
                            .flatMap(tag -> movieRepository.getByTagId(tag.getId())
                                                           .collectList()
                                                           .single()
                                                           .doOnSuccess(movies -> page.add(new MovieCollection(tag.getValue(), movies))))
                            .then(Mono.just(page));
    }
}
