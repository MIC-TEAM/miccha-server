package com.miccha.server.http;

import com.miccha.server.exception.model.RequestInvalidMovieIdException;
import com.miccha.server.exception.model.RequestParameterNotFoundException;
import com.miccha.server.movie.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class MovieHandler {
    private MovieService movieService;

    public Mono<ServerResponse> getPage(ServerRequest request) {
        int pageOffset = Integer.parseInt(request.queryParam("page").orElse("0"));
        return movieService.getPage(pageOffset)
                           .flatMap(page -> ServerResponse.ok()
                                                          .contentType(MediaType.APPLICATION_JSON)
                                                          .body(BodyInserters.fromValue(page)));
    }

    public Mono<ServerResponse> getCategories(ServerRequest request) {
        return movieService.getCategories()
                           .flatMap(tags -> ServerResponse.ok()
                                                          .contentType(MediaType.APPLICATION_JSON)
                                                          .body(BodyInserters.fromValue(tags)));
    }

    public Mono<ServerResponse> getCategorieContents(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("1"));
        Long category = Long.parseLong(request.queryParam("category").orElse("1"));
        return movieService.getCategoriesContents(page, category)
                           .flatMap(tags -> ServerResponse.ok()
                                                          .contentType(MediaType.APPLICATION_JSON)
                                                          .body(BodyInserters.fromValue(tags)));
    }

    public Mono<ServerResponse> getDetail(ServerRequest request) {
        int movieId;
        try {
            movieId = Integer.parseInt(request.pathVariable("movieId"));
        } catch (Exception e) {
            throw new RequestInvalidMovieIdException();
        }

        return movieService.getDetail(movieId)
                           .flatMap(tags -> ServerResponse.ok()
                                                          .contentType(MediaType.APPLICATION_JSON)
                                                          .body(BodyInserters.fromValue(tags)));
    }

    public Mono<ServerResponse> doSearch(ServerRequest request) {
        final String query = request.queryParam("q").orElseThrow(() -> new RequestParameterNotFoundException());
        return movieService.doSearch(query)
                           .collectList()
                           .flatMap(movies -> ServerResponse.ok()
                                                            .body(BodyInserters.fromValue(movies)));
    }
}
