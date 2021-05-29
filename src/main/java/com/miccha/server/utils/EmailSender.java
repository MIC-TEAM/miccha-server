package com.miccha.server.utils;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class EmailSender {
    private ExecutorService executorService;
    private String sender;
    private Mailer mailer;

    public EmailSender(@NonNull ExecutorService executorService,
                       @NonNull String sender,
                       @NonNull String host,
                       int port) {
        this.executorService = executorService;
        this.sender = sender;
        mailer = MailerBuilder.withSMTPServer(host, port)
                              .withDebugLogging(true)
                              .buildMailer();
    }

    public Mono<Void> send(@NonNull String subject,
                           @NonNull String body,
                           @NonNull String receiver) {
        Email email = EmailBuilder.startingBlank()
                                  .from(sender)
                                  .to(receiver)
                                  .withSubject(subject)
                                  .withPlainText(body)
                                  .buildEmail();
        return Mono.fromFuture(CompletableFuture.runAsync(() -> mailer.sendMail(email), executorService));
    }
}
