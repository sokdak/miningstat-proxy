package me.sokdak.miningstatproxy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.sokdak.miningstatproxy.dto.miner.GMinerStatResponse;
import me.sokdak.miningstatproxy.service.ProxyService;
import me.sokdak.miningstatproxy.service.StatPersistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/stat")
@RequiredArgsConstructor
public class ProxyController {
  private final StatPersistService statPersistService;
  private final ProxyService proxyService;

  @GetMapping("/{rigId}")
  public ResponseEntity<GMinerStatResponse> get(
      @PathVariable String rigId,
      @RequestParam String ip,
      @RequestParam int port,
      @RequestParam String type) {
    return ResponseEntity.ok(proxyService.getMinerStat(ip, port, type));
  }
}
