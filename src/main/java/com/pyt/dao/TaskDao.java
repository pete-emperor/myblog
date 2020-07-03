package com.pyt.dao;

import com.pyt.bean.Task;
import com.pyt.bean.ArticleTask;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by peter on 2020/4/2.
 */
@Mapper
@Component
public interface TaskDao {

    public Task getTask(Task task);

    public void updateTask(Task task);

    public List<ArticleTask> getArticleTask(ArticleTask articleTask);

    public void updateArticleTask(ArticleTask articleTask);
}
