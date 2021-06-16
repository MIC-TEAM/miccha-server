package com.miccha.server.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> routeUser(UserHandler handler) {
        return RouterFunctions.route()
                              .POST("/api/users", handler::signUp)
                              .POST("/api/password", handler::reset)
                              .POST("/api/password/retrieve", handler::sendEmail)
                              .POST("/api/session", handler::login)
                              .build();
    }
}