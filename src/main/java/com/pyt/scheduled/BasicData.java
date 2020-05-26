package com.pyt.scheduled;

import com.pyt.bean.Task;
import com.pyt.bean.WordsReplace;
import com.pyt.service.ArticleService;
import com.pyt.service.BlogService;
import com.pyt.service.TaskService;
import com.pyt.util.QueueUtils;
import com.pyt.util.RedisUtil;
import com.pyt.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Order(value = 1)
public class BasicData implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(BasicData.class);

    public static  List<WordsReplace> wordsReplaceList;

    private ArticleService articleService = SpringUtils.getBean(ArticleService.class);

    @Override
    public void run(String... args) throws Exception {
        Long startTime = System.currentTimeMillis();
        wordsReplaceList = articleService.getWordsReplaceList(null);
        Long endTime = System.currentTimeMillis();
        Long costTime = endTime - startTime;
        logger.info("wordsReplaceList:-----------"+wordsReplaceList.size());
        logger.info("本次加载基础数据耗时："+costTime+"毫秒");
    }

}
