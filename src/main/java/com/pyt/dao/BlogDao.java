package com.pyt.dao;

import com.pyt.bean.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by peter on 2020/4/2.
 */
@Mapper
@Component
public interface BlogDao {
    public List<Blog> getBlogList(Blog blog);
    public void insertBlog(Blog blog);
    public Integer getBlogCount(Blog blog);
}
