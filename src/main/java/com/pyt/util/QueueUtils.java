package com.pyt.util;

import com.pyt.bean.ArticleTask;
import com.pyt.bean.Task;
import com.pyt.bean.WordsReplace;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by PC on 2020/4/2.
 */
public class QueueUtils {

    public static ConcurrentLinkedQueue<Task> taskQueue = new ConcurrentLinkedQueue<Task>();

    public static ConcurrentLinkedQueue<ArticleTask> articleTaskQueue = new ConcurrentLinkedQueue<ArticleTask>();

    public static ConcurrentLinkedQueue<Map<String,Object>> secondUrlQueue = new ConcurrentLinkedQueue<Map<String,Object>>();

}
