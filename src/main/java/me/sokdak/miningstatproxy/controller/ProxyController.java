package me.sokdak.miningstatproxy.controller;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.sokdak.miningstatproxy.dto.miner.GMinerStatResponse;
import me.sokdak.miningstatproxy.property.SecurityProperties;
import me.sokdak.miningstatproxy.service.ProxyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/stat")
@RequiredArgsConstructor
public class ProxyController {
  private final SecurityProperties securityConfig;
  private final ProxyService proxyService;

  @GetMapping("/{rigId}")
  public ResponseEntity<GMinerStatResponse> get(
      @PathVariable String rigId,
      @RequestParam String ip,
      @RequestParam int port,
      @RequestParam String type,
      @RequestParam String key)
      throws IOException {
    if (!key.equals(securityConfig.getApiKey()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "api key does not match");
    return ResponseEntity.ok(proxyService.getMinerStat(ip, port, type));
  }
}
