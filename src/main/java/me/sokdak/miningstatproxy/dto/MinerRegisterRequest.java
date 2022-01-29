package me.sokdak.miningstatproxy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MinerRegisterRequest {
  public final String rigId;
  public final String ip;
  public final String port;
  public final String type;
}
