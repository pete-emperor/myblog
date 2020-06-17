package com.pyt.scheduled;


import com.pyt.bean.*;
import com.pyt.service.ArticleService;
import com.pyt.service.BlogService;
import com.pyt.util.QueueUtils;
import com.pyt.util.RedisUtil;
import com.pyt.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Order(value = 2)
public class ArticleWebSpider implements ApplicationRunner {

	private static Logger logger = LoggerFactory.getLogger(ArticleWebSpider.class);

	@Resource
	private RedisUtil redisUtil;

	private static List<String> urlList = new ArrayList<String>();

	private ArticleService articleService = SpringUtils.getBean(ArticleService.class);

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		while (true) {
			if (!QueueUtils.articleTaskQueue.isEmpty() && null != BasicData.wordsReplaceList &&
					BasicData.wordsReplaceList.size() > 0) {
				ArticleTask articleTask = QueueUtils.articleTaskQueue.poll();
				this.ScanSpider(articleTask);
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.info(e.getMessage());
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

		// StringBuffer buffer = getPageContent("UTF-8","https://www.csdn.net/nav/java");
		//logger.info(buffer);
		//ScanSpider(articleTask);
		StringBuffer s = new StringBuffer("<img alt=\"雷军：小米金融旗下香港虚拟银行天星银行正式开业\" src=\"https://cms-bucket.ws.126.net/2020/0611/f70958b0j00qbqzqm001cc000ga00d9c.jpg\" width=\"600\">");
		List l = getPicUrl(s);
		System.out.println(l.get(0));
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
		logger.info("secondUrlList.size:"+secondUrlList.size());
		for(int i = 0;i<secondUrlList.size();i++){
			String pageUrl = secondUrlList.get(i);
			Article article = new Article();
			StringBuffer sbPage = getPageContent(pageCharSet,pageUrl);
			//logger.info(sbPage);

			Pattern titlePre = Pattern.compile(titleRegex0);
			Matcher mreTitle = titlePre.matcher(sbPage);

			while(mreTitle.find()){
				String title = getRegContent(mreTitle.group(0),ignoreStr,titleRegex1,titleRegex2,titleRegex3,splitStr);
				for(WordsReplace wp:BasicData.wordsReplaceList){
					if(title.indexOf(wp.getOldWord()) != -1){
						title = title.replace(wp.getOldWord(),wp.getNewWord());
					}else if(title.indexOf(wp.getNewWord()) != -1){
						title = title.replace(wp.getOldWord(),wp.getNewWord());
					}
				}
				article.setTitle(title);
			}
			if(null != articleTask.getIllegalStr()){
				boolean b = false;
				String illegalStrs[] = articleTask.getIllegalStr().split(",");
				for(String s:illegalStrs){
					if(article.getTitle().contains(s)){
						b = true;
						break;
					}
				}
				if(b) continue;
			}

			List<Article> existA = articleService.getArticleList(article);
			if(!(null != existA && existA.size() > 0)){

				List<String> jpgList = getPicUrl(sbPage);
				for(String jpg:jpgList){
					String tempJpg = "";
					if(!jpg.startsWith("http")){
						tempJpg = articleTask.getImgPre() + jpg;
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					String replacePath = File.separator + "attachment" + File.separator + sdf.format(new Date())  + File.separator;
					String filePath = articleTask.getPathPre() + File.separator + "attachment" + File.separator + sdf.format(new Date()) + File.separator;
					boolean b = downLoadPic(tempJpg,filePath);
					if(b){
						int le = jpg.split("/").length;
						sbPage = new StringBuffer(sbPage.toString().replace(jpg,replacePath+jpg.split("/")[le-1]).replace("<p>&nbsp;</p>",""));
					}
				}

				Pattern contentPre = Pattern.compile(contentRegex0);
				Matcher mreContent = contentPre.matcher(sbPage);
				while(mreContent.find()){
					String content = getRegContent(mreContent.group(0),ignoreStr,contentRegex1,contentRegex2,contentRegex3,splitStr);
					for(WordsReplace wp:BasicData.wordsReplaceList){
						if(content.indexOf(wp.getOldWord()) != -1){
							content = content.replace(wp.getOldWord(),wp.getNewWord());
						}else if(content.indexOf(wp.getNewWord()) != -1){
							content = content.replace(wp.getOldWord(),wp.getNewWord());
						}
					}
					article.setContent(content);
				}
				articleService.insertArticle(article);
				Integer articleId = article.getId();
				String articleCategory = articleTask.getArticleCategory();
				if(null != articleCategory && !articleCategory.equals("")){
					String typeArray []  =  articleCategory.split(",");
					for(int a = 0; a<typeArray.length;a++){
						ArticleCategoryMapping acm = new ArticleCategoryMapping();
						acm.setArticle_id(articleId);
						acm.setCategory_id(Integer.valueOf(typeArray[a]));
						articleService.insertArCaMa(acm);
					}
				}
			}else{
				if(articleTask.getRepeat() < articleTask.getMaxRepeat()){
					logger.info("=======================================");
					logger.info(article.getTitle());
					logger.info("该文章已存在，将不再收录！！");
					logger.info("=======================================");
					articleTask.setRepeat(articleTask.getRepeat()+1);
				}else{
					logger.info("=======================================");
					logger.info("超出重复数将停止此任务的全部录入");
					logger.info("=======================================");
					break;
				}

			}
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
			logger.info(e.getMessage());
		}finally {
			try{
				if(null != br) br.close();
			}catch(Exception e) {
				logger.info(e.getMessage());
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
				logger.info(indexUrlTemp);
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
				if(null != indexUrlTemp && !"".equals(indexUrlTemp)
						&& !firstUrlList.contains(indexUrlTemp) && firstUrlList.size() < articleTask.getPageSize()){
					firstUrlList.add(indexUrlTemp);
					logger.info(indexUrlTemp);
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

	private static List<String> getPicUrl(StringBuffer s){

		Pattern p = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic)\\b)[^>]*>", Pattern.CASE_INSENSITIVE);
		Matcher ma = p.matcher(s);
		List<String> list = new ArrayList<String>();
		String rg = "((https|http):){0,1}/{1,2}(?!(\\.jpg|\\.jpg|\\.bmp|\\.png|\\.tif|\\.gif|\\.pcx|\\.tga|\\.exif|\\.fpx|\\.svg|\\.psd|\\.cdr|\\.pcd|\\.dxf|\\.ufo|\\.eps|\\.ai|\\.raw|\\.WMF|\\.webp))."
				+ "+?(\\.jpg|\\.jpg|\\.bmp|\\.png|\\.tif|\\.gif|\\.pcx|\\.tga|\\.exif|\\.fpx|\\.svg|\\.psd|\\.cdr|\\.pcd|\\.dxf|\\.ufo|\\.eps|\\.ai|\\.raw|\\.WMF|\\.webp)";
		Pattern p1 = Pattern.compile(rg);
		while(ma.find()) {
			Matcher ma1 = p1.matcher(ma.group(0));
			while(ma1.find()) {
				if(!list.contains(ma1.group(0)))
					list.add(ma1.group(0));
			}
		}
		return list;
	}


	private static boolean downLoadPic(String picUrl,String path) {
		try {
			File file = new File(path);
			if(!file.exists()){
				file.mkdirs();
			}
			logger.info("-----------------------------------");
			logger.info(picUrl);
			logger.info("-----------------------------------");
			URL url = new URL(picUrl);
			URLConnection urc =  url.openConnection();
			InputStream inputStream = urc.getInputStream();
			ByteArrayOutputStream data = new ByteArrayOutputStream();
			//设置接收附件最大20MB
			byte [] fileByte = new byte[15*1024*1024];
			int len =0;

			while((len=inputStream.read(fileByte))!=-1) {
				data.write(fileByte,0,len);
			}
			FileOutputStream fos = new FileOutputStream(path+picUrl.split("/")[(picUrl.split("/").length-1)]);
			fos.write(data.toByteArray());
			/*InputStream is = urc.getInputStream();
			byte [] b = new byte[1024];
			int i = 0;
			while((i = is.read(b) )!= -1) {
				fos.write(b);
			}*/
			fos.flush();
			//is.close();
			fos.close();
		} catch (MalformedURLException e) {
			logger.info(e.getMessage());
			return false;
		} catch (IOException e) {
			logger.info(e.getMessage());
			return false;
		}
		return true;
	}
}  
