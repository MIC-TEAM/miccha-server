package com.miccha.server.exception;

import com.google.common.collect.ImmutableMap;
import com.miccha.server.ErrorCode;
import com.miccha.server.exception.model.MicchaException;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Order(-2)
public class MicchaExceptionHandler extends AbstractErrorWebExceptionHandler {
    private static final String KEY_FOR_ERROR_CODE = "errorCode";
    private static final String KEY_FOR_ERROR_MESSAGE = "errorMessage";

    public MicchaExceptionHandler(@NonNull ErrorAttributes errorAttributes,
                                  @NonNull WebProperties.Resources resources,
                                  @NonNull ApplicationContext applicationContext,
                                  @NonNull ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::getErrorResponse);
    }

    private Mono<ServerResponse> getErrorResponse(ServerRequest request) {
        Throwable throwable = getError(request);
        if (throwable instanceof MicchaException) {
            return renderMicchaException((MicchaException) throwable);
        }
        return renderUnknownException(throwable);
    }

    private Mono<ServerResponse> renderMicchaException(MicchaException micchaException) {
        Map<String, Object> body = ImmutableMap.<String, Object>builder()
                                               .put(KEY_FOR_ERROR_CODE, micchaException.getErrorCode().getCode())
                                               .put(KEY_FOR_ERROR_MESSAGE, micchaException.getErrorCode().name())
                                               .build();
        return ServerResponse.status(micchaException.getHttpResponseStatus())
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(BodyInserters.fromValue(body));
    }

    private Mono<ServerResponse> renderUnknownException(Throwable throwable) {
        Map<String, Object> body = ImmutableMap.<String, Object>builder()
                                               .put(KEY_FOR_ERROR_CODE, ErrorCode.UNKNOWN_ERROR.getCode())
                                               .put(KEY_FOR_ERROR_MESSAGE, throwable.getMessage())
                                               .build();
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(BodyInserters.fromValue(body));
    }
}
