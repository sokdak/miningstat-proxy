package me.sokdak.miningstatproxy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.sokdak.miningstatproxy.client.SimpleClient;
import me.sokdak.miningstatproxy.dto.miner.GMinerStatResponse;
import me.sokdak.miningstatproxy.dto.miner.TRexMinerStatResponse;
import me.sokdak.miningstatproxy.dto.miner.TeamRedMinerStatResponse;
import me.sokdak.miningstatproxy.property.MinerTemplateProperties;
import me.sokdak.miningstatproxy.service.mapper.GMinerMapper;
import me.sokdak.miningstatproxy.service.mapper.TRexMinerMapper;
import me.sokdak.miningstatproxy.service.mapper.TeamRedMinerMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProxyService {
  private final MinerTemplateProperties minerTemplateProperties;
  private final SimpleClient simpleClient;
  private final ObjectMapper objectMapper;

  public GMinerStatResponse getMinerStat(String ip, int port, String type) throws IOException {
    switch (type) {
      case "gminer":
        return getGminerStat(ip, port);
      case "tredminer":
        return getTRminerStat(ip, port);
      case "trexminer":
        return getTrexminerStat(ip, port);
      default:
        throw new RuntimeException("minerType %s is not supported".formatted(type));
    }
  }

  public GMinerStatResponse getGminerStat(String ip, int port) {
    log.info("> getGminerStat: {}:{}", ip, port);
    String url = String.format(minerTemplateProperties.getGminerHttpTemplate(), ip, port);
    GMinerStatResponse response = simpleClient.sendGet(URI.create(url), GMinerStatResponse.class);
    log.info("> succeeded: {}", response);
    return GMinerMapper.map(response);
  }

  public GMinerStatResponse getTRminerStat(String ip, int port) throws IOException {
    log.info("> getTRMinerStat: {}:{}", ip, port);

    // open
    Socket socket = new Socket(ip, port);
    DataInputStream input = new DataInputStream(socket.getInputStream());
    DataOutputStream output = new DataOutputStream(socket.getOutputStream());

    if (socket.isConnected()) {
      log.debug(">> socket connected to {}:{}", ip, port);
    } else {
      log.error(">> failed to connect to {}:{}", ip, port);
    }

    // write
    String requestBodyBase64 = minerTemplateProperties.getTredminerRpcTemplate();
    log.debug(
        ">> write body: {}",
        new String(Base64.getDecoder().decode(requestBodyBase64), StandardCharsets.UTF_8));
    output.write(Base64.getDecoder().decode(requestBodyBase64));

    // read
    byte[] resultBytes = input.readAllBytes();
    log.debug(">> read {}bytes from stream", resultBytes.length);

    String result = new String(resultBytes, StandardCharsets.UTF_8);
    log.debug(">> string converted: {}", result);

    // close
    if (!socket.isClosed()) {
      socket.close();
    }

    TeamRedMinerStatResponse response =
        objectMapper.readValue(result, TeamRedMinerStatResponse.class);
    log.info("> succeeded: {}", response);

    return TeamRedMinerMapper.map(response);
  }

  public GMinerStatResponse getTrexminerStat(String ip, int port) {
    log.info(String.format("> getTrexminerStat: %s:%s", ip, port));
    String url = String.format(minerTemplateProperties.getTrexminerHttpTemplate(), ip, port);
    TRexMinerStatResponse response =
        simpleClient.sendGet(URI.create(url), TRexMinerStatResponse.class);
    log.info("> succeeded: {}", response);

    return TRexMinerMapper.map(response);
  }
}
