package com.miccha.server.user;

import com.miccha.server.user.model.EmailChangeRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@Service
public class EmailChangeService {
    private final EmailChangeRepository repository;

    public Mono<String> registerEmailChange(long userId, @NonNull String newEmail) {
        final String token = UUID.randomUUID().toString();
        return repository.save(new EmailChangeRequest(token, userId, newEmail)).map(request -> token);
    }

    public Mono<EmailChangeRequest> getEmailChangeRequest(@NonNull String token) {
        return repository.findByToken(token);
    }
}
