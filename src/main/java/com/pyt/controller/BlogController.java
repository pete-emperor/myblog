package com.pyt.controller;

import com.pyt.bean.Blog;
import com.pyt.bean.Task;
import com.pyt.bean.UserInfo;
import com.pyt.service.BlogService;
import com.pyt.service.TaskService;
import com.pyt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by PC on 2020/4/1.
 */
@RestController
public class BlogController {

    @Autowired
    private BlogService blogService;

    @RequestMapping("/getBlogList")
    public List<Blog> getBlogList(){
        return  blogService.getBlogList(null);
    }

    public static void main(String[] args) {
        String path = ClassUtils.getDefaultClassLoader().getResource("views/page").getPath();
        System.out.println(path.substring(1,path.length()));
    }


}
