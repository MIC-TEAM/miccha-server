package com.miccha.server.movie;

import com.miccha.server.config.Config;
import com.miccha.server.movie.model.Movie;
import com.miccha.server.movie.model.MovieCollection;
import com.miccha.server.movie.model.MovieDetail;
import com.miccha.server.movie.model.TagCollection;

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
    private DirectorRepository directorRepository;
    private ActorRepository actorRepository;

    public Mono<List<MovieCollection>> getPage(int pageOffset) {
        List<MovieCollection> page = new CopyOnWriteArrayList<>();
        return tagRepository.getWithBounds(pageOffset * config.getPageSize(), config.getPageSize())
                            .flatMap(tag -> movieRepository.getByTagId(tag.getId())
                                                           .collectList()
                                                           .single()
                                                           .doOnSuccess(movies -> page.add(new MovieCollection(tag.getValue(), movies))))
                            .then(Mono.just(page));
    }

    public Mono<List<TagCollection>> getCategories() {
        List<TagCollection> tags = new CopyOnWriteArrayList<>();
        return tagRepository.getAllTags()
                            .flatMap(tag -> Mono.just(tags.add(new TagCollection(tag.getId(), tag.getValue()))))
                            .then(Mono.just(tags));
    }

    public Mono<List<Movie>> getCategoriesContents(int page, Long category) {
        return movieRepository.getByTagId(category)
                              .index()
                              .filter(x -> x.getT1() >= (page - 1) * config.getPageSize() &&
                              x.getT1() < page * config.getPageSize())
                              .map(x -> x.getT2())
                              .collectList();

                              
    }

    public Mono<Movie> getDetail(int movieId) {
        return movieRepository.get(movieId)
                              .flatMap(movie -> {
                                  Mono<List<String>> directors = directorRepository.getAllByMovieId(movieId).collectList();
                                  Mono<List<String>> actors = actorRepository.getAllByMovieId(movieId).collectList();
                                  Mono<List<String>> tags = tagRepository.getAllByMovieId(movieId).collectList();
                                  return Mono.zip(directors, actors, tags)
                                             .map(tuple -> {
                                                 movie.setDetails(new MovieDetail(tuple.getT1(), tuple.getT2(), tuple.getT3()));
                                                 return movie;
                                             });
                              });
    }
}
