package com.miccha.server.user;

import com.miccha.server.user.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<Boolean> existsByEmail(String email);
    Mono<User> findByToken(String token);
    Mono<User> findByEmail(String email);
    Mono<User> getByRefreshToken(String refreshToken);
}
