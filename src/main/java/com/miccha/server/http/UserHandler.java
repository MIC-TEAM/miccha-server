package com.miccha.server.http;

import com.miccha.server.ErrorCode;
import com.miccha.server.exception.MicchaException;
import com.miccha.server.http.model.OperationResult;
import com.miccha.server.user.UserService;
import com.miccha.server.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
        Mono<Void> operation = request.bodyToMono(User.class)
                                      .single()
                                      .flatMap(user -> userService.signUp(user));
        return createResponse(operation);
    }

    public Mono<ServerResponse> reset(ServerRequest request) {
        Mono<Void> operation = request.bodyToMono(User.class)
                                      .single()
                                      .flatMap(user -> userService.reset(user));
        return createResponse(operation);
    }

    public Mono<ServerResponse> sendEmail(ServerRequest request) {
        Mono<Void> operation = request.bodyToMono(User.class)
                                      .single()
                                      .flatMap(user -> userService.sendEmail(user));
        return createResponse(operation);
    }

    private Mono<ServerResponse> createResponse(Mono<Void> operation) {
        return operation.thenReturn(ErrorCode.SUCCESS)
                        .onErrorResume(MicchaException.class, e -> Mono.just(e.getErrorCode()))
                        .flatMap(errorCode -> {
                            OperationResult operationResult = OperationResult.builder()
                                                                             .errorCode(errorCode.getCode())
                                                                             .build();

                            if (errorCode.isSuccess()) {
                                return ServerResponse.ok()
                                                     .contentType(MediaType.APPLICATION_JSON)
                                                     .body(BodyInserters.fromValue(operationResult));
                            } else {
                                return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                     .contentType(MediaType.APPLICATION_JSON)
                                                     .body(BodyInserters.fromValue(operationResult));
                            }
                        });
    }
}
