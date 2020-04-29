package com.pyt.scheduled;


import com.pyt.bean.Article;
import com.pyt.bean.Blog;
import com.pyt.bean.Task;
import com.pyt.bean.ArticleTask;
import com.pyt.service.ArticleService;
import com.pyt.service.BlogService;
import com.pyt.util.QueueUtils;
import com.pyt.util.RedisUtil;
import com.pyt.util.SpringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Order(value = 1)
public class ArticleWebSpider implements ApplicationRunner {

	@Resource
	private RedisUtil redisUtil;

	private static List<String> urlList = new ArrayList<String>();

	private ArticleService articleService = SpringUtils.getBean(ArticleService.class);

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		while (true) {
			if (!QueueUtils.articleTaskQueue.isEmpty()) {
				ArticleTask articleTask = QueueUtils.articleTaskQueue.poll();
				this.ScanSpider(articleTask);
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public static void main(String args[]){
        ArticleTask articleTask = new ArticleTask();
        articleTask.setId(1);
        articleTask.setIndexUrl("https://blog.csdn.net/qq_38963960");
        articleTask.setFirstUrlRegex("0000999000099900009990000");
        articleTask.setSecondUrlRegex("https://blog.csdn.net/qq_38963960/article/details/\\d{8,}999000099900009990000");
        articleTask.setTitleRegex("<h1 class=\"title-article\">[\\s\\S]*</h1>9990000999<h1 class=\"title-article\">999</h1>");
        articleTask.setContentRegex("<div class=\"htmledit_views\" id=\"content_views\">[\\s\\S]*<div class=\"more-toolbox\">99900009990000999<div class=\"more-toolbox\">");
        articleTask.setIgnoreStr("0000");
        articleTask.setSplitStr("999");

        //getPageContent(articleTask.getIndexUrl());

        //ScanSpider(articleTask);
    }

	public void ScanSpider(ArticleTask articleTask) {
		Integer id = articleTask.getId();
		String indexUrl = articleTask.getIndexUrl();
		String splitStr = articleTask.getSplitStr();
		String ignoreStr = articleTask.getIgnoreStr();
		String pageCharSet = articleTask.getPageCharSet();

		String firstUrlRegex = articleTask.getFirstUrlRegex();
		String secondUrlRegex = articleTask.getSecondUrlRegex();


		String titleRegex = articleTask.getTitleRegex();
		String titleRegex0 = titleRegex.split(splitStr)[0];
		String titleRegex1 = titleRegex.split(splitStr)[1];
		String titleRegex2 = titleRegex.split(splitStr)[2];
		String titleRegex3 = titleRegex.split(splitStr)[3];

		String contentRegex = articleTask.getContentRegex();
		String contentRegex0 =  contentRegex.split(splitStr)[0];
		String contentRegex1 =  contentRegex.split(splitStr)[1];
		String contentRegex2 =  contentRegex.split(splitStr)[2];
		String contentRegex3 =  contentRegex.split(splitStr)[3];
		Integer type = articleTask.getType();


		List<String> firstUrlList = new ArrayList<String>();
		List<String> secondUrlList = new ArrayList<String>();
		getPageUrl(articleTask,firstUrlList,secondUrlList,splitStr,indexUrl,ignoreStr,firstUrlRegex,secondUrlRegex);
		System.out.println("secondUrlList.size:"+secondUrlList.size());
		for(int i = 0;i<secondUrlList.size();i++){
			String pageUrl = secondUrlList.get(i);
			Article article = new Article();
			StringBuffer sbPage = getPageContent(pageCharSet,pageUrl);

			Pattern titlePre = Pattern.compile(titleRegex0);
			Matcher mreTitle = titlePre.matcher(sbPage);

			while(mreTitle.find()){
				String title = getRegContent(mreTitle.group(0),ignoreStr,titleRegex1,titleRegex2,titleRegex3,splitStr);
				article.setTitle(title);
			}

			Pattern contentPre = Pattern.compile(contentRegex0);
			Matcher mreContent = contentPre.matcher(sbPage);

			while(mreContent.find()){
				String content = getRegContent(mreContent.group(0),ignoreStr,contentRegex1,contentRegex2,contentRegex3,splitStr);
				article.setContent(content);
			}
			articleService.insertArticle(article);
		}

	}

	public static StringBuffer getPageContent(String pageCharSet,String pageUrl){

		URLConnection pageUrlConn = null;
		BufferedReader br = null;
		URL url = null;
		StringBuffer sbPage = new StringBuffer();
		try {
			url = new URL(pageUrl);
			pageUrlConn = url.openConnection();
			pageUrlConn.setConnectTimeout(10000);
			br = new BufferedReader(new InputStreamReader(pageUrlConn.getInputStream(), pageCharSet));
			String strTemp = "";
			while ((strTemp = br.readLine()) != null) {
				sbPage.append(strTemp);
			}
			br.close();

		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try{
				if(null != br) br.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return sbPage;
	}

	//爬取分页url
	public static void getPageUrl(ArticleTask articleTask,List <String> firstUrlList,List<String> secondUrlList,String splitStr,String indexUrl,String ignoreStr,String firstUrlRegex,String secondUrlRegex){

            StringBuffer sbPage = getPageContent(articleTask.getPageCharSet(),indexUrl);
			String secondUrlRegex0 = secondUrlRegex.split(splitStr)[0];
			String secondUrlRegex1 = secondUrlRegex.split(splitStr)[1];
			String secondUrlRegex2 = secondUrlRegex.split(splitStr)[2];
			String secondUrlRegex3 = secondUrlRegex.split(splitStr)[3];

			Pattern preSecondUrl = Pattern.compile(secondUrlRegex0);
			Matcher mreSecondUrl = preSecondUrl.matcher(sbPage);

			while (mreSecondUrl.find()) {
				String indexUrlTemp = getRegContent(mreSecondUrl.group(0),ignoreStr,secondUrlRegex1,secondUrlRegex2,secondUrlRegex3,splitStr);
				if(null != indexUrlTemp && !"".equals(indexUrlTemp) && !secondUrlList.contains(indexUrlTemp)){
					System.out.println(indexUrlTemp);
					secondUrlList.add(indexUrlTemp);
					/*Map<String,Object> map = new HashMap<String,Object>();
					map.put("indexUrl",indexUrlTemp);
					map.put("articleTask",articleTask);
					QueueUtils.secondUrlQueue.add(map);*/
				}
			}

			String firstUrlRegex0 = firstUrlRegex.split(splitStr)[0];
			String firstUrlRegex1 = firstUrlRegex.split(splitStr)[1];
			String firstUrlRegex2 = firstUrlRegex.split(splitStr)[2];
			String firstUrlRegex3 = firstUrlRegex.split(splitStr)[3];

			if(!firstUrlRegex0.equals(ignoreStr)){
				Pattern preFirstUrl = Pattern.compile(firstUrlRegex0);
				Matcher mreFirstUrl = preFirstUrl.matcher(sbPage);

				while (mreFirstUrl.find()) {
					String indexUrlTemp = getRegContent(mreFirstUrl.group(0),ignoreStr,firstUrlRegex1,firstUrlRegex2,firstUrlRegex3,splitStr);
					if(null != indexUrlTemp && !"".equals(indexUrlTemp) && !firstUrlList.contains(indexUrlTemp)){
						firstUrlList.add(indexUrlTemp);
						System.out.println(indexUrlTemp);
						//if(firstUrlList.size()>2000) break;
						getPageUrl(articleTask,firstUrlList,secondUrlList,splitStr,indexUrlTemp,ignoreStr,firstUrlRegex,secondUrlRegex);
					}
				}
			}

	}

	public static String getRegContent(String temp,String ignoreStr,String urlRegex1,String urlRegex2,String urlRegex3,String splitStr){

		if(urlRegex2.equals(ignoreStr)){
			if(!urlRegex3.equals(ignoreStr)){
				temp = temp.substring(0,temp.lastIndexOf(urlRegex3));
			}
		}else{
			if(!urlRegex3.equals(ignoreStr)){
				temp = temp.substring(temp.indexOf(urlRegex2) + urlRegex2.length(),temp.lastIndexOf(urlRegex3));
			}else{
				temp = temp.substring(temp.indexOf(urlRegex2) + urlRegex2.length(),temp.length());
			}
		}
		String urlTemp = "";
		if(!urlRegex1.equals(ignoreStr)){
			urlTemp = urlRegex1 + temp;
		}else {
			urlTemp = temp;
		}

		return  urlTemp;
	}

	private static List<String> getPicUrl(String s){
		String rg = "http://(?!(\\.jpg|\\.jpg|\\.bmp|\\.png|\\.tif|\\.gif|\\.pcx|\\.tga|\\.exif|\\.fpx|\\.svg|\\.psd|\\.cdr|\\.pcd|\\.dxf|\\.ufo|\\.eps|\\.ai|\\.raw|\\.WMF|\\.webp))."
				+ "+?(\\.jpg|\\.jpg|\\.bmp|\\.png|\\.tif|\\.gif|\\.pcx|\\.tga|\\.exif|\\.fpx|\\.svg|\\.psd|\\.cdr|\\.pcd|\\.dxf|\\.ufo|\\.eps|\\.ai|\\.raw|\\.WMF|\\.webp)";

		Pattern pt = Pattern.compile(s);

		Matcher ma = pt.matcher(s);

		List<String> list = new ArrayList<String>();

		while(ma.find()) {
			list.add(ma.group(0));
		}
		return list;
	}

	private static void downLoadPic(String picUrl,String path) {
		try {
			URL url = new URL(picUrl);
			URLConnection urc =  url.openConnection();
			FileOutputStream fos = new FileOutputStream(path+picUrl.split("/")[picUrl.split("/").length-1]);
			InputStream is = urc.getInputStream();
			byte [] b = new byte[1024];
			int i = 0;
			while((i = is.read(b) )!= -1) {
				fos.write(b);
			}
			is.close();
			fos.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}  
