package me.sokdak.miningstatproxy.domain;

import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.*;

@Data
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

  private String minerType;
  private ZonedDateTime createdTime;
  private ZonedDateTime updatedTime;

  @OneToMany(orphanRemoval = true)
  private List<Device> devices;
}
