package me.sokdak.miningstatproxy.service;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.sokdak.miningstatproxy.domain.Miner;
import me.sokdak.miningstatproxy.dto.MinerStatResponse;
import me.sokdak.miningstatproxy.dto.miner.GMinerStatResponse;
import me.sokdak.miningstatproxy.repository.MinerRepository;
import me.sokdak.miningstatproxy.service.mapper.DeviceMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatPersistService {
  private final MinerRepository minerRepository;
  private final ProxyService proxyService;

  public List<MinerStatResponse> list() {
    List<Miner> miners = minerRepository.findAll();
    log.info(">> list all miners: {}", (long) miners.size());

    return miners.stream().map(DeviceMapper::map).collect(Collectors.toList());
  }

  public MinerStatResponse get(String rigId) {
    log.info(">> get miner: {}", rigId);
    Optional<Miner> miner = minerRepository.findByRigId(rigId);
    if (miner.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "rigId not found");
    return DeviceMapper.map(miner.get());
  }

  @Transactional
  public void register(String rigId, String ip, String port, String type) {
    this.update(rigId, ip, port, type);
  }

  @Transactional
  public MinerStatResponse update(String rigId, String ip, String port, String minerType) {
    Optional<Miner> miner = minerRepository.findById(ip);

    Miner minerEntity;
    if (miner.isEmpty()) {
      log.info(">> data not found for miner ip: {}, type: {}", ip, minerType);

      // get
      GMinerStatResponse response =
          proxyService.getMinerStat(ip, Integer.parseInt(port), minerType);

      // create
      minerEntity =
          DeviceMapper.map(
              ip, minerType, rigId, port, ZonedDateTime.now(), ZonedDateTime.now(), response);
    } else {
      log.info(
          ">> previous data found miner rigId: {}, ip: {}, type: {}, lastUpdatedTime: {}",
          miner.get().getRigId(),
          miner.get().getIp(),
          miner.get().getMinerType(),
          miner.get().getUpdatedTime());

      // get
      GMinerStatResponse response =
          proxyService.getMinerStat(ip, Integer.parseInt(port), minerType);

      // update
      minerEntity =
          DeviceMapper.map(
              miner.get().getIp(),
              miner.get().getMinerType(),
              miner.get().getRigId(),
              miner.get().getApiPort(),
              miner.get().getCreatedTime(),
              ZonedDateTime.now(),
              response);
    }

    Miner persistedMiner = minerRepository.saveAndFlush(minerEntity);
    return DeviceMapper.map(persistedMiner);
  }

  public void delete(String rigId) {
    log.info(">> delete miner {}", rigId);
    minerRepository.deleteByRigId(rigId);
  }

  public void setMinerAsErrorState(String rigId) {
    log.info(">> set miner {} as error status", rigId);
    Optional<Miner> miner = minerRepository.findByRigId(rigId);

    if (miner.isPresent()) {
      Miner erroredMiner = miner.get();

      // check miner is already errored state
      if (erroredMiner.getDevices().size() > 0) {
        log.info("miner {} is in error state", rigId);
        erroredMiner.setUpdatedTime(ZonedDateTime.now());
        erroredMiner.setDevices(Collections.emptyList());
        minerRepository.saveAndFlush(erroredMiner);
      } else {
        log.info("miner {} is already in error state, ignoring...", rigId);
      }
    } else {
      log.error("miner {} is not present, ignoring..", rigId);
    }
  }
}
