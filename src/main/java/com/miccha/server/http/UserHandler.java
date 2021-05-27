package com.miccha.server.http;

import com.miccha.server.ErrorCode;
import com.miccha.server.exception.MicchaException;
import com.miccha.server.exception.NoException;
import com.miccha.server.http.model.OperationResult;
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
                      .thenReturn(OperationResult.builder()
                                                 .errorCode(NoException.INSTANCE.getErrorCode())
                                                 .build())
                      .onErrorResume(MicchaException.class, e -> Mono.just(OperationResult.builder()
                                                                                          .errorCode(e.getErrorCode())
                                                                                          .build()))
                      .flatMap(result -> ServerResponse.ok()
                                                       .contentType(MediaType.APPLICATION_JSON)
                                                       .body(BodyInserters.fromValue(result)));
    }
}
