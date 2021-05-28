package com.miccha.server.user;

import com.miccha.server.user.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<Boolean> existsByEmail(String email);
}
