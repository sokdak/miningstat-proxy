package me.sokdak.miningstatproxy.service.mapper;

import java.time.ZonedDateTime;
import java.util.stream.Collectors;
import me.sokdak.miningstatproxy.domain.Device;
import me.sokdak.miningstatproxy.domain.Miner;
import me.sokdak.miningstatproxy.dto.miner.GMinerStatResponse;

public class DeviceMapper {
  public static GMinerStatResponse map(Miner miner) {
    return new GMinerStatResponse(
        miner.getUptime(),
        miner.getServer(),
        miner.getUser(),
        miner.getAlgorithm(),
        miner.getDevices().stream()
            .map(
                d ->
                    new GMinerStatResponse.Device(
                        d.getGpuId(),
                        d.getName(),
                        d.getSpeed(),
                        d.getAcceptedShares(),
                        d.getRejectedShares(),
                        d.getInvalidShares(),
                        d.getStaleShares(),
                        d.getFan(),
                        d.getTemperature(),
                        d.getMemoryTemperature(),
                        d.getPowerUsage()))
            .collect(Collectors.toList()));
  }

  public static Miner map(
      String ip,
      String minerType,
      ZonedDateTime createdTime,
      ZonedDateTime updatedTime,
      GMinerStatResponse response) {
    return new Miner(
        ip,
        response.getUptime(),
        response.getServer(),
        response.getUser(),
        response.getAlgorithm(),
        minerType,
        createdTime,
        updatedTime,
        response.getDevices().stream()
            .map(
                m ->
                    new Device(
                        Device.toGlobalId(ip, String.valueOf(m.getGpuId())),
                        m.getGpuId(),
                        m.getName(),
                        m.getSpeed(),
                        m.getAcceptedShares(),
                        m.getRejectedShares(),
                        m.getInvalidShares(),
                        m.getStaleShares(),
                        m.getFan(),
                        m.getTemperature(),
                        m.getMemoryTemperature(),
                        m.getPowerUsage()))
            .collect(Collectors.toList()));
  }
}
