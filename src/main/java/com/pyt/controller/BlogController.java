package com.pyt.controller;

import com.pyt.bean.Blog;
import com.pyt.bean.Task;
import com.pyt.bean.UserInfo;
import com.pyt.service.BlogService;
import com.pyt.service.TaskService;
import com.pyt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PC on 2020/4/1.
 */
@RestController
public class BlogController {

    @Value("${page.size}")
    private Integer pageSize;

    @Autowired
    private BlogService blogService;

    @RequestMapping("/getBlogList")
    public List<Blog> getBlogList(HttpServletRequest request){
        Blog blog = new Blog();
        blog.setPageIndex(Integer.valueOf(request.getParameter("pageIndex")));
        blog.setPageSize(Integer.valueOf(request.getParameter("pageSize")));
        return  blogService.getBlogList(blog);
    }

    @RequestMapping("/getBlogPage")
    public Map<String,Integer> getBlogPage(HttpServletRequest request){
        Map<String,Integer> map = new HashMap<>();
        String pageSize2 =  request.getParameter("pageSize");
        if(null != pageSize2 && "" != pageSize2){
            pageSize = Integer.valueOf(pageSize2);
        }
        Integer pageCount = blogService.getBlogCount(null);
        map.put("pageCount",pageCount%10==0?(pageCount/pageSize):((pageCount/pageSize)+1));
        return map;
    }

    public static void main(String[] args) {
        String path = ClassUtils.getDefaultClassLoader().getResource("views/page").getPath();
        System.out.println(path.substring(1,path.length()));
    }


}
