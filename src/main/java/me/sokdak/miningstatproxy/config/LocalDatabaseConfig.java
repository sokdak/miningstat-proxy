package me.sokdak.miningstatproxy.config;

import java.sql.SQLException;
import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class LocalDatabaseConfig {
  @Bean
  @Profile("local")
  public Server h2Server() {
    Server h2Server;
    try {
      h2Server = Server.createTcpServer();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to start H2 server: ", e);
    }
    return h2Server;
  }
}
