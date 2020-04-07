package com.pyt.service;

import com.pyt.bean.Blog;
import com.pyt.bean.UserInfo;
import com.pyt.dao.BlogDao;
import com.pyt.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by PC on 2020/4/1.
 */
@Service
public class BlogService {

    @Autowired
    private BlogDao blogDao;

    public List<Blog> getBlogList(Blog blog){
        return blogDao.getBlogList(blog);
    }
    public Integer getBlogCount(Blog blog){
        return blogDao.getBlogCount(blog);
    }
    public void insertBlog(Blog blog){
        blogDao.insertBlog(blog);
    }
}
