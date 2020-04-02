package com.pyt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by PC on 2020/4/1.
 */
@Controller
public class PageController {

    @RequestMapping("toIndexPage")
    public String toIndexPage(){
        return "index";
    }

    @RequestMapping("toUserInfoPage")
    public String toUserInfoPage(){
        return "userInfo";
    }

    @RequestMapping("toTargetPage")
    public String toTargetPage(String html){
        System.out.println(html);
        System.out.println(html.replace(".html",""));
        return "page/"+html.replace(".html","");
    }
}
