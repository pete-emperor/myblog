package com.pyt.scheduled;

import com.pyt.bean.Task;
import com.pyt.service.TaskService;
import com.pyt.util.QueueUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by PC on 2020/4/1.
 */
//@PropertySource("classpath:scheduled.properties")
//@Component
//@EnableScheduling
public class BasicTask {

    private static Logger logger = LoggerFactory.getLogger(BasicTask.class);

    @Autowired
    private TaskService taskService;

    public  void run(){
            Task task = taskService.getTask(null);
            if(null != task){
                QueueUtils.taskQueue.offer(task);
                taskService.updateTask(task);
            }
    }

    @Scheduled(cron = "${task.cron}") //每分钟执行一次statusCheck方法
    public void basicDataRefresh() {
        Long startTime = System.currentTimeMillis();
        this.run();
        Long endTime = System.currentTimeMillis();
        Long costTime = endTime - startTime;
        logger.info("本次加载基础数据耗时："+costTime+"毫秒");
    }

}
