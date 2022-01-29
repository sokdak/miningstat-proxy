package me.sokdak.miningstatproxy.config;

import me.sokdak.miningstatproxy.filter.AuthFilter;
import me.sokdak.miningstatproxy.property.SecurityFilterProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FilterConfig {
  @Bean
  @Profile("!local || !test")
  public AuthFilter authFilter(SecurityFilterProperties properties) {
    return new AuthFilter(properties);
  }
}
