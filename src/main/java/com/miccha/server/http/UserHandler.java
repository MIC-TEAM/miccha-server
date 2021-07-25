package com.miccha.server.http;

import com.google.common.collect.ImmutableMap;
import com.miccha.server.ErrorCode;
import com.miccha.server.exception.model.EmptyRequestBodyException;
import com.miccha.server.exception.model.RequestMissingTokenException;
import com.miccha.server.security.JWTUtil;
import com.miccha.server.user.UserService;
import com.miccha.server.user.model.User;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import static java.util.Objects.isNull;

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
                                                          .map(user -> Pair.of(loginRequest, user)))
                      .filter(pair -> {
                          return passwordEncoder.encode(pair.getKey().getPassword()).equals(pair.getValue().getPassword());
                      })
                      .flatMap(pair -> {
                          final User user = pair.getValue();
                          final String token = jwtUtil.generateToken(user);

                          Map<String, String> response = new HashMap<>();
                          response.put("accessToken", token);
                          response.put("email", user.getEmail());
                          response.put("name", user.getName());

                          return userService.updateRefreshToken(user)
                                            .flatMap(refreshToken -> {
                                                return ServerResponse
                                                        .ok()
                                                        .cookie(ResponseCookie.from("refreshToken", refreshToken).build())
                                                        .body(BodyInserters.fromValue(response));
                                            });
                      })
                      .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build());
    }

    public Mono<ServerResponse> changeSetting(ServerRequest request) {
        return request.bodyToMono(User.class)
                      .switchIfEmpty(Mono.error(new EmptyRequestBodyException()))
                      .zipWith(getCurrentEmail())
                      .flatMap(tuple -> {
                          final User newSetting = tuple.getT1();
                          final String currentEmail = tuple.getT2();
                          return changeUserName(currentEmail, newSetting.getName())
                                  .then(changePassword(currentEmail, newSetting.getPassword()))
                                  .then(changeEmail(currentEmail, newSetting.getEmail()));
                      })
                      .then(ServerResponse.ok().build());
    }

    private Mono<String> getCurrentEmail() {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(securityContext -> (String) securityContext.getAuthentication().getPrincipal());
    }

    private Mono<Void> changeUserName(String currentEmail, String name) {
        if (isNull(name)) {
            return Mono.empty();
        }
        return userService.changeUserName(currentEmail, name);
    }

    private Mono<Void> changePassword(String currentEmail, String password) {
        if (isNull(password)) {
            return Mono.empty();
        }
        return userService.changePassword(currentEmail, password);
    }

    private Mono<Void> changeEmail(String currentEmail, String newEmail) {
        if (isNull(newEmail)) {
            return Mono.empty();
        }
        return userService.requestChangeEmail(currentEmail, newEmail);
    }

    public Mono<ServerResponse> confirmChangeEmail(ServerRequest request) {
        return request.bodyToMono(User.class)
                      .switchIfEmpty(Mono.error(new EmptyRequestBodyException()))
                      .map(user -> user.getToken())
                      .switchIfEmpty(Mono.error(new RequestMissingTokenException()))
                      .flatMap(token -> userService.changeEmail(token))
                      .then(ServerResponse.ok().build());
    }
}
