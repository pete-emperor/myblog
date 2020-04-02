package com.pyt.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by PC on 2020/4/1.
 */
@PropertySource("classpath:scheduled.properties")
@Component
@EnableScheduling
public class BasicTask {

    private static Logger logger = LoggerFactory.getLogger(BasicTask.class);

    public  void run(){

    }

    @Scheduled(cron = "${task.cron}") //每分钟执行一次statusCheck方法
    public void basicDataRefresh() {
        Long startTime = System.currentTimeMillis();
        Long endTime = System.currentTimeMillis();
        Long costTime = endTime - startTime;
        logger.info("本次加载基础数据耗时："+costTime+"毫秒");
    }

}
