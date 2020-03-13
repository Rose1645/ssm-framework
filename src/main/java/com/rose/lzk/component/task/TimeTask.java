package com.rose.lzk.component.task;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务示例
 */

@Component
@Log4j2
public class TimeTask {

  /**
   * cron表达式：Seconds Minutes Hours DayofMonth Month DayofWeek [Year]
   * 十分钟执行一次
   */
  @Scheduled(cron = "0 0/10 * ? * ?")
  public  void testTask(){
    log.info("定时任务执行");
  }
}
