package com.pyt.service;

import com.pyt.bean.Task;
import com.pyt.bean.ArticleTask;
import com.pyt.bean.UserInfo;
import com.pyt.dao.TaskDao;
import com.pyt.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by PC on 2020/4/1.
 */
@Service
public class TaskService {

    @Autowired
    private TaskDao taskDao;

    public Task getTask(Task task){
        return taskDao.getTask(task);
    }

    public void updateTask(Task task){
        taskDao.updateTask(task);
    }

    public ArticleTask getArticleTask(ArticleTask ArticleTask){
        return taskDao.getArticleTask(ArticleTask);
    }

    public void updateArticleTask(ArticleTask ArticleTask){
        taskDao.updateArticleTask(ArticleTask);
    }



}
