package com.miccha.server.user;

import com.miccha.server.exception.model.*;
import com.miccha.server.security.JWTUtil;
import com.miccha.server.user.model.User;
import com.miccha.server.utils.EmailSender;
import com.miccha.server.utils.PasswordHasher;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.util.Objects.isNull;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private static Pattern specialCharacterPattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
    private static Pattern alphabetPattern = Pattern.compile("[A-Za-z]");
    private static Pattern digitPattern = Pattern.compile("[0-9]");

    private final PasswordHasher passwordHasher;
    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final EmailChangeService emailChangeService;
    private final JWTUtil jwtUtil;

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

    public Mono<Void> sendEmail(@NonNull User user) {
        final UUID uuid = UUID.randomUUID();
        return Mono.just(user)
                   .doOnNext(userValue -> {
                       if (isNull(userValue.getEmail())) {
                           throw new RequestMissingEmailException();
                       }
                   })
                   .flatMap(userValue -> userRepository.findByEmail(userValue.getEmail()))
                   .single()
                   .flatMap(foundUser -> {
                       foundUser.setToken(uuid.toString());
                       return userRepository.save(foundUser);
                   })
                   .flatMap(updatedUser -> {
                       final String subject = "Here is your token for miccha password reset";
                       return emailSender.send(subject, updatedUser.getToken(), updatedUser.getEmail());
                   })
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

    public Mono<User> findByUsername(@NonNull String username) {
        return userRepository.findByEmail(username);
    }

    public Mono<Void> requestChangeEmail(@NonNull String currentEmail, @NonNull String newEmail) {
        return userRepository.findByEmail(currentEmail)
                             .switchIfEmpty(Mono.error(new EmailNotFoundException()))
                             .flatMap(user -> emailChangeService.registerEmailChange(user.getId(), newEmail))
                             .switchIfEmpty(Mono.error(new EmailChangeTokenUpdateFailedException()))
                             .flatMap(token -> {
                                 final String subject = "this is the token for email change";
                                 return emailSender.send(subject, token, newEmail);
                             });
    }

    public Mono<Void> changeEmail(@NonNull String token) {
        return emailChangeService.getEmailChangeRequest(token)
                                 .switchIfEmpty(Mono.error(new EmailChangeTokenNotFoundException()))
                                 .flatMap(request -> {
                                     return userRepository.findById(request.getUserId())
                                                          .map(user -> {
                                                              user.setEmail(request.getEmail());
                                                              return user;
                                                          });
                                 })
                                 .flatMap(userRepository::save)
                                 .switchIfEmpty(Mono.error(new EmailUpdateFailedException()))
                                 .then(Mono.empty());
    }

    public Mono<Void> changeUserName(@NonNull String currentEmail, @NonNull String newUserName) {
        return userRepository.findByEmail(currentEmail)
                             .switchIfEmpty(Mono.error(new EmailNotFoundException()))
                             .flatMap(user -> {
                                 user.setName(newUserName);
                                 return userRepository.save(user);
                             })
                             .switchIfEmpty(Mono.error(new NameUpdateFailedException()))
                             .then(Mono.empty());
    }

    public Mono<Void> changePassword(@NonNull String currentEmail, @NonNull String newPassword) {
        if (isValidPassword(newPassword) == false) {
            return Mono.error(new InvalidPasswordException());
        }

        return userRepository.findByEmail(currentEmail)
                             .switchIfEmpty(Mono.error(new EmailNotFoundException()))
                             .flatMap(user -> {
                                 user.setPassword(passwordHasher.getHahsedPassword(newPassword));
                                 return userRepository.save(user);
                             })
                             .switchIfEmpty(Mono.error(new PasswordUpdateFailedException()))
                             .then(Mono.empty());
    }

    public Mono<String> updateRefreshToken(@NonNull User user) {
        final String refreshToken = UUID.randomUUID().toString();
        user.setRefreshToken(refreshToken);
        return userRepository.save(user)
                             .doOnSuccess(updatedUser -> {
                                 if (isNull(updatedUser)) {
                                     throw new RefreshTokenUpdateFailedException();
                                 }
                             })
                             .then(Mono.just(refreshToken));
    }

    public Mono<String> createAccessToken(@NonNull String refreshToken) {
        return userRepository.getByRefreshToken(refreshToken)
                             .switchIfEmpty(Mono.error(new InvalidRefreshTokenException()))
                             .map(jwtUtil::generateToken);
    }
}
