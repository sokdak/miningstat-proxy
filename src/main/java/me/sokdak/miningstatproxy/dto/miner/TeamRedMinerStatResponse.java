package me.sokdak.miningstatproxy.dto.miner;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class TeamRedMinerStatResponse {
  private Summary summary;
  private Device devs;
  private DeviceDetail devdetails;
  private Pool pools;

  @Getter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Summary {
    @JsonProperty("SUMMARY")
    private List<SummaryInfo> summaryInfo;

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
    public static class SummaryInfo {
      private int elasped;

      @JsonProperty("MHS 30s")
      private double hashrate;

      private int accepted;
      private int rejected;

      @JsonProperty("Hardware Errors")
      private int hardwareErrors;

      private int stale;
    }
  }

  @Getter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Device {
    @JsonProperty("DEVS")
    private List<DeviceInfo> devices;

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
    public static class DeviceInfo {
      private int gpu;
      private float temperature;

      @JsonProperty("Fan Percent")
      private int fanSpeed;

      @JsonProperty("GPU Clock")
      private int gpuClock;

      @JsonProperty("Memory Clock")
      private int memClock;

      @JsonProperty("GPU Voltage")
      private double gpuVoltage;

      private int accepted;
      private int rejected;

      @JsonProperty("Hardware Errors")
      private int hardwareErrors;

      @JsonProperty("MHS 30s")
      private double hashrate;

      @JsonProperty("Device Elapsed")
      private int elapsed;

      private double temperatureJnct;
      private double temperatureMem;

      @JsonProperty("GPU Power")
      private double power;
    }
  }

  @Getter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DeviceDetail {
    @JsonProperty("DEVDETAILS")
    private List<DeviceDetailInfo> deviceDetailInfo;

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
    public static class DeviceDetailInfo {
      @JsonProperty("ID")
      private int id;

      private String model;
      private String kernel;

      @JsonProperty("Device Path")
      private String devicePath;
    }
  }

  @Getter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Pool {
    @JsonProperty("POOLS")
    private List<PoolInfo> poolInfos;

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
    public static class PoolInfo {
      @JsonProperty("URL")
      private String url;

      private String user;
    }
  }
}
