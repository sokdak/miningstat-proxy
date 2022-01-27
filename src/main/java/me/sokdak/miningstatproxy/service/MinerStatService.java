package me.sokdak.miningstatproxy.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.sokdak.miningstatproxy.domain.Miner;
import me.sokdak.miningstatproxy.dto.miner.GMinerStatResponse;
import me.sokdak.miningstatproxy.repository.MinerRepository;
import me.sokdak.miningstatproxy.service.mapper.DeviceMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinerStatService {
  private final MinerRepository minerRepository;

  public List<GMinerStatResponse> list() {
    List<Miner> miners = minerRepository.findAll();
    log.info(">> list all miners: {}", (long) miners.size());

    return miners.stream().map(DeviceMapper::map).collect(Collectors.toList());
  }

  @Transactional
  public GMinerStatResponse update(String ip, String minerType, GMinerStatResponse response) {
    Optional<Miner> miner = minerRepository.findById(ip);

    Miner minerEntity;
    if (miner.isEmpty()) {
      log.info(">> not found miner ip: {}, type: {}", ip, minerType);

      // create
      minerEntity =
          DeviceMapper.map(ip, minerType, ZonedDateTime.now(), ZonedDateTime.now(), response);
    } else {
      log.info(
          ">> found miner ip: {}, type: {}, lastUpdatedTime: {}",
          miner.get().getIp(),
          miner.get().getMinerType(),
          miner.get().getUpdatedTime());

      // update
      minerEntity =
          DeviceMapper.map(
              ip, minerType, miner.get().getCreatedTime(), ZonedDateTime.now(), response);
    }

    Miner persistedMiner = minerRepository.saveAndFlush(minerEntity);
    return DeviceMapper.map(persistedMiner);
  }

  public void delete(String ip) {
    log.info(">> delete miner {}", ip);
    minerRepository.deleteById(ip);
  }
}
