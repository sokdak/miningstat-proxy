package me.sokdak.miningstatproxy.service.mapper;

import java.util.ArrayList;
import java.util.List;
import me.sokdak.miningstatproxy.dto.miner.GMinerStatResponse;
import me.sokdak.miningstatproxy.dto.miner.TeamRedMinerStatResponse;

public class TeamRedMinerMapper {
  public static GMinerStatResponse map(TeamRedMinerStatResponse response) {
    List<GMinerStatResponse.Device> devices = new ArrayList<>();
    for (int i = 0; i < response.getDevdetails().getDeviceDetailInfo().size(); i++) {
      var a = response.getDevs().getDevices().get(i);
      var b = response.getDevdetails().getDeviceDetailInfo().get(i);

      devices.add(
          new GMinerStatResponse.Device(
              b.getId(),
              b.getModel(),
              (int) Math.floor(a.getHashrate() * 1000 * 1000),
              a.getAccepted(),
              a.getRejected(),
              a.getHardwareErrors(),
              0,
              a.getFanSpeed(),
              (int) Math.floor(a.getTemperature()),
              (int) Math.floor(a.getTemperatureMem()),
              (int) Math.floor(a.getPower())));
    }

    return new GMinerStatResponse(
        response.getSummary().getSummaryInfo().get(0).getElasped(),
        response.getPools().getPoolInfos().get(0).getUrl(),
        response.getPools().getPoolInfos().get(0).getUser(),
        response.getDevdetails().getDeviceDetailInfo().get(0).getKernel(),
        devices);
  }
}
