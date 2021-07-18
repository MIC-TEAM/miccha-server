package com.miccha.server.user;

import com.miccha.server.user.model.EmailChangeRequest;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface EmailChangeRepository extends ReactiveCrudRepository<EmailChangeRequest, String> {
    Mono<EmailChangeRequest> save(EmailChangeRequest request);
    Mono<EmailChangeRequest> findByToken(String token);
}
