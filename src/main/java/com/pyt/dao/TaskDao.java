package com.pyt.dao;

import com.pyt.bean.Task;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Created by peter on 2020/4/2.
 */
@Mapper
@Component
public interface TaskDao {

    public Task getTask(Task task);

    public void updateTask(Task task);

}
