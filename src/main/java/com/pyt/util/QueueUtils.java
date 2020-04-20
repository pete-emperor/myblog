package com.pyt.util;

import com.pyt.bean.ArticleTask;
import com.pyt.bean.Task;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by PC on 2020/4/2.
 */
public class QueueUtils {

    public static ConcurrentLinkedQueue<Task> taskQueue = new ConcurrentLinkedQueue<Task>();

    public static ConcurrentLinkedQueue<ArticleTask> articleTaskQueue = new ConcurrentLinkedQueue<ArticleTask>();
}
