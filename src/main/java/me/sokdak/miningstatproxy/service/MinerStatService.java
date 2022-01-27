package me.sokdak.miningstatproxy.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.sokdak.miningstatproxy.domain.Miner;
import me.sokdak.miningstatproxy.dto.miner.GMinerStatResponse;
import me.sokdak.miningstatproxy.repository.MinerRepository;
import me.sokdak.miningstatproxy.service.mapper.DeviceMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinerStatService {
  private final MinerRepository minerRepository;

  public List<GMinerStatResponse> list() {
    List<Miner> miners = minerRepository.findAll();
    return miners.stream().map(DeviceMapper::map).collect(Collectors.toList());
  }
}
