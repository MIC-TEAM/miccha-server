package com.miccha.server.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class MovieListRouter {
    @Bean
    public RouterFunction<ServerResponse> route(MovieListHandler handler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/v1/movies").and(
                        RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::hello);
    }
}
