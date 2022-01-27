package me.sokdak.miningstatproxy.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Device {
  @Id private String id;
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

  public String getId(String rigId, String devNo) {
    return "%s_%s".formatted(rigId, devNo);
  }
}
