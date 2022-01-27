package me.sokdak.miningstatproxy.service.mapper;

import java.util.stream.Collectors;
import me.sokdak.miningstatproxy.dto.miner.GMinerStatResponse;
import me.sokdak.miningstatproxy.dto.miner.TRexMinerStatResponse;

public class TRexMinerMapper {
  public static GMinerStatResponse map(TRexMinerStatResponse response) {
    return new GMinerStatResponse(
        response.getUptime(),
        response.getActivePool().getUrl(),
        response.getActivePool().getUser(),
        response.getAlgorithm(),
        response.getGpus().stream()
            .map(
                g ->
                    new GMinerStatResponse.Device(
                        g.getDeviceId(),
                        g.getName(),
                        g.getHashrate(),
                        g.getShares().getAcceptedCount(),
                        g.getShares().getRejectedCount(),
                        g.getShares().getInvalidCount(),
                        0,
                        g.getFanSpeed(),
                        g.getTemperature(),
                        g.getMemoryTemperature(),
                        g.getPower()))
            .collect(Collectors.toList()));
  }
}
