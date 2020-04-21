package com.pyt.util;



import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by PC on 2020/4/21.
 */
public class Test {
    private static class innerWebClient{
        private static final  WebClient webClient = new WebClient();
    }

    /**
     * 获取指定网页实体
     * @param url
     * @return
     */
    public static HtmlPage getHtmlPage(String url){
        //调用此方法时加载WebClient
        WebClient webClient = innerWebClient.webClient;
        String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0";
        webClient.getBrowserVersion().setUserAgent(userAgent);
        // 取消 JS 支持
        webClient.setJavaScriptEnabled(false);
        // 取消 CSS 支持
        webClient.setCssEnabled(false);
        HtmlPage page=null;
        try{
            // 获取指定网页实体
            page = (HtmlPage) webClient.getPage(url);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return page;
    }


    public static void main(String[] args) throws Exception {
        // 获取指定网页实体
        HtmlPage page = getHtmlPage("http://data.eastmoney.com/zjlx/detail.html");

        //System.out.println(page.asXml());   //asXml()是以xml格式显示
        // 获取搜索输入框
        //HtmlInput input = page.getHtmlElementById("sb_form_q");
        // 往输入框 “填值”
        // 获取搜索按钮
        //HtmlInput btn = page.getHtmlElementById("sb_form_go");
        // “点击” 搜索
        //HtmlPage page2 = btn.click();


        //System.out.println(page2.asXml());
        // 选择元素

        getTitle(page,"//*[@id=\"dt_1\"]/tbody//tr","//*[@id=\"PageCont\"]/a[8]");

    }

    public static void getTitle(HtmlPage page,String reg1,String reg2){
        List<HtmlElement> spanList= (List<HtmlElement>) page.getByXPath(reg1);
        System.out.println("----------------------------------------------------");
        for(int i=0;i<spanList.size();i++) {
            // 输出新页面的文本
            System.out.println(i+1+"、"+spanList.get(i).asText());
        }
        System.out.println("----------------------------------------------------");
        List<HtmlElement> spanList2= (List<HtmlElement>) page.getByXPath(reg2);
        for(int i=0;i<spanList2.size();i++) {
            // 输出新页面的文本
            HtmlAnchor ha = (HtmlAnchor) spanList2.get(i);
            try {
                page = ha.click();
                page.wait(1000);
                getTitle(page, reg1, reg2);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        System.out.println("----------------------------------------------------");
    }



}
