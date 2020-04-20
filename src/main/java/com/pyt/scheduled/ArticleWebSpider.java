package com.pyt.scheduled;


import com.pyt.bean.Blog;
import com.pyt.bean.Task;
import com.pyt.bean.ArticleTask;
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

	private BlogService blogService = SpringUtils.getBean(BlogService.class);

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

	public void ScanSpider(ArticleTask articleTask) {
		Integer id = articleTask.getId();
		String indexUrl = articleTask.getIndexUrl();
		String splitStr = articleTask.getSplitStr();
		String ignoreStr = articleTask.getIgnoreStr();

		String firstUrlRegex = articleTask.getFirstUrlRegex();
		String firstUrlRegex0 = firstUrlRegex.split(splitStr)[0];
		String firstUrlRegex1 = firstUrlRegex.split(splitStr)[1];
		String firstUrlRegex2 = firstUrlRegex.split(splitStr)[2];
		String firstUrlRegex3 = firstUrlRegex.split(splitStr)[3];

		String secondUrlRegex = articleTask.getSecondUrlRegex();
		String secondUrlRegex0 = secondUrlRegex.split(splitStr)[0];
		String secondUrlRegex1 = secondUrlRegex.split(splitStr)[1];
		String secondUrlRegex2 = secondUrlRegex.split(splitStr)[2];
		String secondUrlRegex3 = secondUrlRegex.split(splitStr)[3];

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
		firstUrlList = getPageUrl(firstUrlList,secondUrlList,indexUrl,ignoreStr,firstUrlRegex,secondUrlRegex);





	}

	//爬取分页url
	public static List<String> getPageUrl(List <String> firstUrlList,List<String> secondUrlList,String splitStr,String indexUrl,String ignoreStr,String firstUrlRegex,String secondUrlRegex){





		String secondUrlRegex0 = secondUrlRegex.split(splitStr)[0];
		String secondUrlRegex1 = secondUrlRegex.split(splitStr)[1];
		String secondUrlRegex2 = secondUrlRegex.split(splitStr)[2];
		String secondUrlRegex3 = secondUrlRegex.split(splitStr)[3];

		URLConnection urlconn = null;
		BufferedReader br = null;
		URL url = null;
		try {
			url = new URL(indexUrl);
			urlconn = url.openConnection();
			urlconn.setConnectTimeout(10000);
			br = new BufferedReader(new InputStreamReader(urlconn.getInputStream(), "UTF-8"));

			StringBuffer sbPage = new StringBuffer();
			String strTemp = "";
			while((strTemp = br.readLine()) != null){
				sbPage.append(strTemp);
			}
			br.close();



			Pattern preSecondUrl = Pattern.compile(secondUrlRegex0);
			Matcher mreSecondUrl = preSecondUrl.matcher(sbPage.toString());




			while (mreFirstUrl.find()) {
				String indexUrlTemp = getRegContent(ignoreStr,firstUrlRegex,);
				if(!firstUrlList.contains(indexUrlTemp)){
					firstUrlList.add(indexUrlTemp);
					getPageUrl(firstUrlList,secondUrlList,indexUrlTemp,ignoreStr,Regex0,Regex1,Regex2,Regex3);
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return urlList;

	}

	public static String getRegContent(String ignoreStr,String urlRegex,String splitStr,String sbPage){

		String urlRegex0 = urlRegex.split(splitStr)[0];
		String urlRegex1 = urlRegex.split(splitStr)[1];
		String urlRegex2 = urlRegex.split(splitStr)[2];
		String urlRegex3 = urlRegex.split(splitStr)[3];

		Pattern pre = Pattern.compile(urlRegex0);
		Matcher mre = pre.matcher(sbPage.toString());
		String temp = mre.group(0);
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
		}else{
			urlTemp = temp;
		}

		return  urlTemp;
	}

	//获取每页各个文章的url
	public void getPageArticleUrl(){

	}

	//获取文章页面的标题和内容
	public void getArticleTitleContent(){

	}

}  
