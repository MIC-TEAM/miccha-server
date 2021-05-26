package com.miccha.server.config;

import com.github.jasync.r2dbc.mysql.JasyncConnectionFactory;
import com.github.jasync.sql.db.Configuration;
import com.github.jasync.sql.db.mysql.pool.MySQLConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("mysql")
@ConditionalOnProperty(prefix = "mysql", value = {"host", "port", "username", "password", "database"})
public class MysqlConfig {
    private String host;
    private int port;
    private String username;
    private String password;
    private String database;

    public ConnectionFactory createConnectionFactory() {
        return new JasyncConnectionFactory(new MySQLConnectionFactory(new Configuration(
                username,
                host,
                port,
                password,
                database
        )));
    }
}
