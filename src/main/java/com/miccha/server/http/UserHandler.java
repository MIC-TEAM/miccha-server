package com.miccha.server.http;

import com.google.common.collect.ImmutableMap;
import com.miccha.server.ErrorCode;
import com.miccha.server.security.JWTUtil;
import com.miccha.server.user.UserService;
import com.miccha.server.user.model.User;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class UserHandler {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private JWTUtil jwtUtil;

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

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(User.class)
                      .flatMap(loginRequest -> userService.findByUsername(loginRequest.getEmail())
                                                          .map(user -> Pair.of(loginRequest, user))
                      .filter(pair -> {
                          return passwordEncoder.encode(pair.getKey().getPassword()).equals(pair.getValue().getPassword());
                      })
                      .flatMap(pair -> ServerResponse.ok()
                                                 .body(BodyInserters.fromValue(ImmutableMap.of("accessToke", jwtUtil.generateToken(pair.getValue()))))))
                      .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build());
    }
}
