package com.miccha.server.user;

import com.miccha.server.exception.DuplicateEmailException;
import com.miccha.server.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Mono<Void> signUp(@NonNull User user) {
        return userRepository.existsByEmail(user.getEmail())
                             .single()
                             .doOnSuccess(exists -> {
                                 if (exists) {
                                     throw new DuplicateEmailException();
                                 }
                             })
                             .then(userRepository.save(user))
                             .single()
                             .then(Mono.empty());
    }
}
