package com.pyt.controller;

import com.pyt.bean.Blog;
import com.pyt.bean.Task;
import com.pyt.bean.UserInfo;
import com.pyt.service.BlogService;
import com.pyt.service.TaskService;
import com.pyt.service.UserService;
import com.pyt.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private BlogService blogService;

    @RequestMapping("/getBlogList")
    public List<Blog> getBlogList(HttpServletRequest request){
        Blog blog = new Blog();
        List<Blog> list = null;
        String pageIndex = request.getParameter("pageIndex");
        String pageSize = request.getParameter("pageSize");

        if(!redisUtil.hasKey(pageIndex+"_"+pageSize)){
            blog.setPageIndex(Integer.valueOf(pageIndex));
            blog.setPageSize(Integer.valueOf(pageSize));
            list =  blogService.getBlogList(blog);
            redisUtil.set(pageIndex+"_"+pageSize,list,30l);
        }else{
            list = (List<Blog>)redisUtil.get(pageIndex+"_"+pageSize);
        }
        if(redisUtil.hasKey("page")){
            redisUtil.set("page",redisUtil.get("page")+","+pageIndex+"_"+pageSize);
        }else{
            redisUtil.set("page",pageIndex+"_"+pageSize);
        }

        return  list;
    }

    @RequestMapping("/getBlogPage")
    public Map<String,Integer> getBlogPage(HttpServletRequest request){
        Map<String,Integer> map = new HashMap<>();
        String pageSize2 =  request.getParameter("pageSize");
        if(null != pageSize2 && "" != pageSize2){
            pageSize = Integer.valueOf(pageSize2);
        }
        Integer pageCount = 0;
        if(redisUtil.hasKey("pageCount")){
            pageCount = (Integer) redisUtil.get("pageCount");
        }else{
            pageCount = blogService.getBlogCount(null);
            redisUtil.set("pageCount",pageCount);
        }
        map.put("pageCount",pageCount%10==0?(pageCount/pageSize):((pageCount/pageSize)+1));
        return map;
    }



}
