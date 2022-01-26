package me.sokdak.miningstatproxy.dto.miner;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GMinerStatResponse {
    private int uptime;
    private String server;
    private String user;
    private String algorithm;
    private List<Device> devices;

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Device {
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
