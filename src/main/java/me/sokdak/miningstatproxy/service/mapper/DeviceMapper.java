package me.sokdak.miningstatproxy.service.mapper;

import java.util.stream.Collectors;
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
}
