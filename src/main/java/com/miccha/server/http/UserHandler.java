package com.miccha.server.http;

import com.google.common.collect.ImmutableMap;
import com.miccha.server.ErrorCode;
import com.miccha.server.user.UserService;
import com.miccha.server.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class UserHandler {
    private UserService userService;

    public Mono<ServerResponse> signUp(ServerRequest request) {
        return request.bodyToMono(User.class)
                      .single()
                      .flatMap(user -> userService.signUp(user))
                      .then(createSuccessResponse());
    }

    public Mono<ServerResponse> reset(ServerRequest request) {
        return request.bodyToMono(User.class)
                      .single()
                      .flatMap(user -> userService.reset(user))
                      .then(createSuccessResponse());
    }

    public Mono<ServerResponse> sendEmail(ServerRequest request) {
        return request.bodyToMono(User.class)
                      .single()
                      .flatMap(user -> userService.sendEmail(user))
                      .then(createSuccessResponse());
    }

    private Mono<ServerResponse> createSuccessResponse() {
        return ServerResponse.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(BodyInserters.fromValue(ImmutableMap.builder()
                                                                       .put("errorCode", ErrorCode.SUCCESS.getCode())
                                                                       .build()));
    }
}
