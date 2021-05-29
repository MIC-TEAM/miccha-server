package com.miccha.server.user;

import com.miccha.server.exception.*;
import com.miccha.server.user.model.User;
import com.miccha.server.utils.PasswordHasher;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.util.Objects.isNull;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private static Pattern specialCharacterPattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
    private static Pattern alphabetPattern = Pattern.compile("[A-Za-z]");
    private static Pattern digitPattern = Pattern.compile("[0-9]");


    private final PasswordHasher passwordHasher;
    private final UserRepository userRepository;

    public Mono<Void> signUp(@NonNull User user) {
        return Mono.just(user)
                   .doOnNext(userValue -> {
                       if (EmailValidator.getInstance().isValid(userValue.getEmail()) == false) {
                           throw new InvalidEmailException();
                       }
                   })
                   .doOnNext(userValue -> {
                       if (isValidPassword(user.getPassword()) == false) {
                           throw new InvalidPasswordException();
                       }

                       user.setPassword(passwordHasher.getHahsedPassword(user.getPassword()));
                   })
                   .flatMap(userValue -> userRepository.existsByEmail(userValue.getEmail()))
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

    public Mono<Void> reset(@NonNull User user) {
        return Mono.just(user)
                   .doOnNext(userValue -> {
                       if (isNull(user.getToken())) {
                           throw new RequestMissingTokenException();
                       }

                       if (isNull(user.getPassword())) {
                           throw new RequestMissingPasswordException();
                       }

                       if (isValidPassword(user.getPassword()) == false) {
                           throw new InvalidPasswordException();
                       }
                   })
                   .flatMap(userValue -> userRepository.findByToken(userValue.getToken()))
                   .single()
                   .flatMap(foundUser -> {
                       foundUser.setPassword(passwordHasher.getHahsedPassword(user.getPassword()));
                       foundUser.setToken(null);
                       return userRepository.save(foundUser);
                   })
                   .single()
                   .then(Mono.empty());
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 10) {
            return false;
        }

        int matchCount = 0;

        if (specialCharacterPattern.matcher(password).find()) {
            matchCount++;
        }
        if (alphabetPattern.matcher(password).find()) {
            matchCount++;
        }
        if (digitPattern.matcher(password).find()) {
            matchCount++;
        }

        return matchCount >= 2;
    }
}
