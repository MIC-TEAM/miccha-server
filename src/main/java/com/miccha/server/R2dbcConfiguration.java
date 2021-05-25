package com.miccha.server;

import com.miccha.server.config.MysqlConfig;
import io.r2dbc.spi.ConnectionFactory;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

@AllArgsConstructor
@Configuration
public class R2dbcConfiguration extends AbstractR2dbcConfiguration {
    private MysqlConfig mysqlConfig;

    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        return mysqlConfig.createConnectionFactory();
    }
}
