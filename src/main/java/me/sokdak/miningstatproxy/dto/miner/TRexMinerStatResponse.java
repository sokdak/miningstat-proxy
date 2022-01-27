package me.sokdak.miningstatproxy.dto.miner;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TRexMinerStatResponse {
  private int acceptedCount;
  private String algorithm;
  private double difficulty;
  private int gpuTotal;
  private PoolInfo activePool;
  private List<GpuInfo> gpus;
  private int hashrate;
  private int rejectedCount;
  private int solvedCount;
  private int uptime;

  @Getter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PoolInfo {
    private int ping;
    private int retries;
    private String url;
    private String user;
  }

  @Getter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class GpuInfo {
    private int deviceId;
    private String efficiency;
    private int fanSpeed;
    private int hashrate;
    private float intensity;
    private String name;
    private int temperature;
    private int memoryTemperature;
    private ShareInfo shares;
    private float lhrTune;
    private int mclock;
    private int cclock;
    private int power;

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ShareInfo {
      private int acceptedCount;
      private int invalidCount;
      private int rejectedCount;
      private int solvedCount;
    }
  }
}
