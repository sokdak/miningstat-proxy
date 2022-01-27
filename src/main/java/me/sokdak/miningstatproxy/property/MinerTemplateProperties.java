package me.sokdak.miningstatproxy.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "template")
public class MinerTemplateProperties {
  private final String gminerHttpTemplate;
  private final String trexminerHttpTemplate;
  private final String tredminerRpcTemplate;
}
