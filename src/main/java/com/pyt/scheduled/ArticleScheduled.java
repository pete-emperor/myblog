package com.pyt.scheduled;


import com.pyt.bean.Article;
import com.pyt.bean.ArticleTask;
import com.pyt.service.ArticleService;
import com.pyt.util.QueueUtils;
import com.pyt.util.RedisUtil;
import com.pyt.util.SpringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ArticleScheduled /*implements ApplicationRunner */{

	//@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		while (true) {
			if (!QueueUtils.secondUrlQueue.isEmpty() && null != BasicData.wordsReplaceList &&
					BasicData.wordsReplaceList.size() > 0) {
				Map<String,Object> map = QueueUtils.secondUrlQueue.poll();
				this.InsertArticle(map);
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void InsertArticle(Map<String,Object> map){
		String pageUrl = (String)map.get("indexUrl");
		System.out.println("================"+pageUrl);
		ArticleTask articleTask = (ArticleTask)map.get("articleTask");
		Article article = new Article();
		String indexUrl = articleTask.getIndexUrl();
		String splitStr = articleTask.getSplitStr();
		String ignoreStr = articleTask.getIgnoreStr();

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
			StringBuffer sbPage = ArticleWebSpider.getPageContent(null,pageUrl);

			Pattern titlePre = Pattern.compile(titleRegex0);
			Matcher mreTitle = titlePre.matcher(sbPage);

			while(mreTitle.find()){
				String title = ArticleWebSpider.getRegContent(mreTitle.group(0),ignoreStr,titleRegex1,titleRegex2,titleRegex3,splitStr);
				article.setTitle(title);
			}

			Pattern contentPre = Pattern.compile(contentRegex0);
			Matcher mreContent = contentPre.matcher(sbPage);

			while(mreContent.find()){
				String content = ArticleWebSpider.getRegContent(mreContent.group(0),ignoreStr,contentRegex1,contentRegex2,contentRegex3,splitStr);
				article.setContent(content);
			}
			//articleService.insertArticle(article);
	}

	public void ScanSpider(ArticleTask articleTask) {
		Integer id = articleTask.getId();
		String indexUrl = articleTask.getIndexUrl();
		String splitStr = articleTask.getSplitStr();
		String ignoreStr = articleTask.getIgnoreStr();

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
		ArticleWebSpider.getPageUrl(articleTask,firstUrlList,secondUrlList,splitStr,indexUrl,ignoreStr,firstUrlRegex,secondUrlRegex);
		System.out.println("secondUrlList.size:"+secondUrlList.size());
		for(int i = 0;i<secondUrlList.size();i++){
			String pageUrl = secondUrlList.get(i);
			Article article = new Article();
			StringBuffer sbPage = ArticleWebSpider.getPageContent(null,pageUrl);

			Pattern titlePre = Pattern.compile(titleRegex0);
			Matcher mreTitle = titlePre.matcher(sbPage);

			while(mreTitle.find()){
				String title = ArticleWebSpider.getRegContent(mreTitle.group(0),ignoreStr,titleRegex1,titleRegex2,titleRegex3,splitStr);
				article.setTitle(title);
			}

			Pattern contentPre = Pattern.compile(contentRegex0);
			Matcher mreContent = contentPre.matcher(sbPage);

			while(mreContent.find()){
				String content = ArticleWebSpider.getRegContent(mreContent.group(0),ignoreStr,contentRegex1,contentRegex2,contentRegex3,splitStr);
				article.setContent(content);
			}
			//articleService.insertArticle(article);
		}
	}
}
