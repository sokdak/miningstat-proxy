package me.sokdak.miningstatproxy.config;

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.sokdak.miningstatproxy.domain.Miner;
import me.sokdak.miningstatproxy.poll.MinerPoolJob;
import me.sokdak.miningstatproxy.repository.MinerRepository;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PoolingJobConfig {
  private final Scheduler scheduler;
  private final MinerRepository minerRepository;

  @PostConstruct
  public void loadMinersToQuartz() {
    List<Miner> miners = minerRepository.findAll();

    miners.forEach(
        m -> {
          String id = m.getRigId();
          String ip = m.getIp();
          String port = m.getApiPort();
          String type = m.getMinerType();

          JobDetail statPoolingJob =
              buildJobDetail(
                  MinerPoolJob.class,
                  Map.of(
                      MinerPoolJob.KEY_MINER_ID,
                      id,
                      MinerPoolJob.KEY_MINER_IP,
                      ip,
                      MinerPoolJob.KEY_MINER_PORT,
                      port,
                      MinerPoolJob.KEY_MINER_TYPE,
                      type));

          try {
            scheduler.scheduleJob(statPoolingJob, buildJobTrigger("* * * * * ?"));
          } catch (SchedulerException e) {
            e.printStackTrace();
          }
        });
  }

  private Trigger buildJobTrigger(String cronString) {
    return TriggerBuilder.newTrigger()
        .withSchedule(CronScheduleBuilder.cronSchedule(cronString))
        .build();
  }

  private JobDetail buildJobDetail(Class<? extends Job> job, Map<String, ?> param) {
    JobDataMap map = new JobDataMap();
    map.putAll(param);

    return JobBuilder.newJob(job).usingJobData(map).build();
  }
}
