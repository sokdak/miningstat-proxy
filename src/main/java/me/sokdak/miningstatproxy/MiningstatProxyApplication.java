package me.sokdak.miningstatproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MiningstatProxyApplication {

  public static void main(String[] args) {
    SpringApplication.run(MiningstatProxyApplication.class, args);
  }
}
