package me.sokdak.miningstatproxy.service.mapper;

import java.time.ZonedDateTime;
import java.util.stream.Collectors;
import me.sokdak.miningstatproxy.domain.Device;
import me.sokdak.miningstatproxy.domain.Miner;
import me.sokdak.miningstatproxy.dto.MinerStatResponse;
import me.sokdak.miningstatproxy.dto.miner.GMinerStatResponse;

public class DeviceMapper {
  public static MinerStatResponse map(Miner miner) {
    return new MinerStatResponse(
        miner.getRigId(),
        miner.getCreatedTime(),
        miner.getUpdatedTime(),
        miner.getUptime(),
        miner.getServer(),
        miner.getUser(),
        miner.getAlgorithm(),
        miner.getDevices().stream()
            .map(
                d ->
                    new MinerStatResponse.MiningDevice(
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
      String rigId,
      String apiPort,
      ZonedDateTime createdTime,
      ZonedDateTime updatedTime,
      GMinerStatResponse response) {
    return new Miner(
        ip,
        response.getUptime(),
        response.getServer(),
        response.getUser(),
        response.getAlgorithm(),
        rigId,
        minerType,
        apiPort,
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
