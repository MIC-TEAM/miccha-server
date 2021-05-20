package com.miccha.server;

import io.r2dbc.spi.*;
import org.junit.jupiter.api.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class R2dbcMysqlTest {
    private static final String MYSQL_HOST = "host";
    private static final int MYSQL_PORT = -1;
    private static final String MYSQL_USER = "user";
    private static final String MYSQL_PASSWORD = "password";
    private static final String MYSQL_DATABASE = "database";

    private static ConnectionFactory connectionFactory;

    @BeforeAll
    public static void setUp() {
        connectionFactory = getConnectionFactory();
    }

    @Order(1)
    @Test
    public void insert_movie() {
        Mono<Connection> connectionMono = Mono.from(connectionFactory.create());
        Mono<Integer> rowsUpdatedMono = connectionMono.single()
                                                      .flatMapMany(conn -> {
                                                          final String query = "INSERT INTO movie(title,description,rating,duration,thumbnail) VALUES (?,?,?,?,?)";
                                                          return conn.createStatement(query)
                                                                     .bind(0, "title")
                                                                     .bind(1, "description")
                                                                     .bind(2, "rating")
                                                                     .bind(3, 3600)
                                                                     .bind(4, "thumbnail")
                                                                     .execute();
                                                      })
                                                      .flatMap(Result::getRowsUpdated)
                                                      .single();

        StepVerifier.create(rowsUpdatedMono)
                    .expectNext(1)
                    .expectComplete()
                    .verify();
    }

    @Order(2)
    @Test
    public void select_movie() {
        Mono<Connection> connectionMono = Mono.from(connectionFactory.create());
        Mono<Long> countMono = connectionMono.single()
                                             .flatMapMany(conn -> {
                                                 final String query = "SELECT * FROM movie WHERE title = ? AND description = ? AND rating = ? AND duration = ? AND thumbnail = ?";
                                                 return conn.createStatement(query)
                                                            .bind(0, "title")
                                                            .bind(1, "description")
                                                            .bind(2, "rating")
                                                            .bind(3, 3600)
                                                            .bind(4, "thumbnail")
                                                            .execute();
                                             })
                                             .flatMap(result -> result.map((row, rowMetadata) -> row.get(0)))
                                             .count();

        StepVerifier.create(countMono)
                    .expectNext(Long.valueOf(1))
                    .expectComplete()
                    .verify();
    }

    @Order(3)
    @Test
    public void delete_movie() {
        Mono<Connection> connectionMono = Mono.from(connectionFactory.create());
        Mono<Integer> countMono = connectionMono.single()
                                                .flatMapMany(conn -> {
                                                    final String query = "DELETE FROM movie WHERE title = ? AND description = ? AND rating = ? AND duration = ? AND thumbnail = ?";
                                                    return conn.createStatement(query)
                                                               .bind(0, "title")
                                                               .bind(1, "description")
                                                               .bind(2, "rating")
                                                               .bind(3, 3600)
                                                               .bind(4, "thumbnail")
                                                               .execute();
                                                })
                                                .flatMap(Result::getRowsUpdated)
                                                .single();

        StepVerifier.create(countMono)
                    .expectNext(1)
                    .expectComplete()
                    .verify();
    }

    private static ConnectionFactory getConnectionFactory() {
        return ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                                        .option(PROTOCOL, "mysql")
                                        .option(DRIVER, "pool")
                                        .option(HOST, MYSQL_HOST)
                                        .option(USER, MYSQL_USER)
                                        .option(PORT, MYSQL_PORT)
                                        .option(PASSWORD, MYSQL_PASSWORD)
                                        .option(DATABASE, MYSQL_DATABASE)
                                        .option(CONNECT_TIMEOUT, Duration.ofSeconds(3))
                                        .option(SSL, true)
                                        .build()
        );
    }
}
