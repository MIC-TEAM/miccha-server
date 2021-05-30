package com.miccha.server.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class MovieRouter {
    @Bean
    public RouterFunction<ServerResponse> routeMovie(MovieHandler handler) {
        return RouterFunctions.route()
                              .GET("/api/home", handler::getPage)
                              .build();
    }
}
