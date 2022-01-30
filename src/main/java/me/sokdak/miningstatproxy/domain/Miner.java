package me.sokdak.miningstatproxy.domain;

import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Miner {
  @Id private String ip;

  private int uptime;
  private String server;

  @Column(name = "user_name")
  private String user;

  private String algorithm;

  private String rigId;
  private String minerType;
  private String apiPort;
  private ZonedDateTime createdTime;
  private ZonedDateTime updatedTime;

  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
  private List<Device> devices;
}
