package me.sokdak.miningstatproxy.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.sokdak.miningstatproxy.dto.MinerRegisterRequest;
import me.sokdak.miningstatproxy.dto.miner.GMinerStatResponse;
import me.sokdak.miningstatproxy.service.StatPersistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/miner")
@RequiredArgsConstructor
public class MinerController {
  private final StatPersistService statPersistService;

  @PostMapping("")
  public ResponseEntity<MinerRegisterRequest> register(@RequestBody MinerRegisterRequest request) {
    statPersistService.register(request.rigId, request.ip, request.port, request.type);
    return ResponseEntity.accepted().body(request);
  }

  @GetMapping("")
  public ResponseEntity<List<GMinerStatResponse>> list() {
    return ResponseEntity.ok(statPersistService.list());
  }

  @GetMapping("{rigId}")
  public ResponseEntity<GMinerStatResponse> get(@RequestParam String rigId) {
    return ResponseEntity.ok(statPersistService.get(rigId));
  }
}
