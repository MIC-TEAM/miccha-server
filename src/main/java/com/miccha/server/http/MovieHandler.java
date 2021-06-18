package com.miccha.server.http;

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
        int pageOffset = Integer.parseInt(request.queryParam("page").orElse("0"));
        return movieService.getPage(pageOffset)
                           .flatMap(page -> ServerResponse.ok()
                                                          .contentType(MediaType.APPLICATION_JSON)
                                                          .body(BodyInserters.fromValue(page)));
    }
}
