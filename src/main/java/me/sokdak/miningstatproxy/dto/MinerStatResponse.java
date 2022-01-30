package me.sokdak.miningstatproxy.dto;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MinerStatResponse {
  private String rigId;
  private ZonedDateTime createdTime;
  private ZonedDateTime updatedTime;

  private Integer uptime;
  private String server;
  private String user;
  private String algorithm;
  private List<MiningDevice> devices;

  @AllArgsConstructor
  public static class MiningDevice {
    private int gpuId;
    private String name;
    private int speed;
    private int acceptedShares;
    private int rejectedShares;
    private int invalidShares;
    private int staleShares;
    private int fan;
    private int temperature;
    private int memoryTemperature;
    private int powerUsage;
  }
}
