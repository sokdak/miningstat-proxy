package me.sokdak.miningstatproxy.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "endpoint")
public class MinerHttpTemplateConfig {
    private final String gminerUriTemplate;
    private final String trexminerUriTemplate;
}
