package me.sokdak.miningstatproxy.poll;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.sokdak.miningstatproxy.service.ProxyService;
import me.sokdak.miningstatproxy.service.StatPersistService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinerPoolJob extends QuartzJobBean {
  private final StatPersistService statPersistService;
  private final ProxyService proxyService;

  public static final String KEY_MINER_ID = "MINER_ID";
  public static final String KEY_MINER_IP = "MINER_IP";
  public static final String KEY_MINER_PORT = "MINER_PORT";
  public static final String KEY_MINER_TYPE = "MINER_TYPE";

  @SneakyThrows
  @Override
  protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    JobDataMap map = context.getJobDetail().getJobDataMap();
    String id = map.getString(KEY_MINER_ID);
    String ip = map.getString(KEY_MINER_IP);
    int port = map.getIntegerFromString(KEY_MINER_PORT);
    String type = map.getString(KEY_MINER_TYPE);

    log.info(">> run job for {} at {} ({}:{}, {})", id, context.getFireTime(), ip, port, type);

    statPersistService.update(id, ip, String.valueOf(port), type);

    log.info(
        "<< end job for {} at {}, next fire at {}",
        id,
        context.getFireTime(),
        context.getNextFireTime());
  }
}
