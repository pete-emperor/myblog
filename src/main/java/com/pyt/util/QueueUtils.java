package com.pyt.util;

import com.pyt.bean.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by PC on 2020/4/2.
 */
public class QueueUtils {

    public static ConcurrentLinkedQueue<Task> taskQueue = new ConcurrentLinkedQueue<Task>();

    public static ConcurrentLinkedQueue<ArticleTask> articleTaskQueue = new ConcurrentLinkedQueue<ArticleTask>();

    public static ConcurrentLinkedQueue<TaskListClass> indexUrlQueue = new ConcurrentLinkedQueue<TaskListClass>();

    public static ConcurrentLinkedQueue<TaskListClass> secondUrlQueue = new ConcurrentLinkedQueue<TaskListClass>();

    public static ConcurrentLinkedQueue<TaskListClass> imgDownLoadQueue = new ConcurrentLinkedQueue<TaskListClass>();

    public static ConcurrentLinkedQueue<Article> articleQueue = new ConcurrentLinkedQueue<Article>();


}
