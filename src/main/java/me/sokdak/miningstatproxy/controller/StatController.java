package me.sokdak.miningstatproxy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.sokdak.miningstatproxy.config.SecurityConfig;
import me.sokdak.miningstatproxy.dto.miner.GMinerStatResponse;
import me.sokdak.miningstatproxy.service.StatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/stat")
@RequiredArgsConstructor
public class StatController {
    private final SecurityConfig securityConfig;
    private final StatService statService;

    @GetMapping("/{rigId}")
    public ResponseEntity<GMinerStatResponse> get(@PathVariable String rigId, @RequestParam String ip, @RequestParam int port,
                                                  @RequestParam String type, @RequestParam String key) throws IOException {
        if (!key.equals(securityConfig.getApiKey()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "api key does not match");

        switch (type) {
            case "gminer":
                return ResponseEntity.ok(statService.getGminerStat(rigId, ip, port));
            case "tredminer":
                return ResponseEntity.ok(statService.getTRminerStat(rigId, ip, port));
            case "trexminer":
                return ResponseEntity.ok(statService.getTrexminerStat(rigId, ip, port));
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("type is not supported %s", type));
        }
    }
}
