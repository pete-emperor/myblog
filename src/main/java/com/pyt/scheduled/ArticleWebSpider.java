package com.pyt.scheduled;


import com.jcraft.jsch.ChannelSftp;
import com.pyt.bean.*;
import com.pyt.service.ArticleService;
import com.pyt.service.BlogService;
import com.pyt.util.*;
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

	private static  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	private ArticleService articleService = SpringUtils.getBean(ArticleService.class);

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		this.ScanSpider();
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

	public void ScanSpider() {

		//产生indexURL及secondURL
		new Thread(new Runnable() {
			public void run() {
				while(true){

					if(!QueueUtils.indexUrlQueue.isEmpty()){
						TaskListClass taskListClass= QueueUtils.indexUrlQueue.poll();
						ArticleTask articleTask = taskListClass.getArticleTask();
						StringBuffer sbPage = getPageContent(articleTask.getPageCharSet(),taskListClass.getDataString());
						String secondUrlRegex0 = articleTask.getSecondUrlRegex().split(articleTask.getSplitStr())[0];
						String secondUrlRegex1 = articleTask.getSecondUrlRegex().split(articleTask.getSplitStr())[1];
						String secondUrlRegex2 = articleTask.getSecondUrlRegex().split(articleTask.getSplitStr())[2];
						String secondUrlRegex3 = articleTask.getSecondUrlRegex().split(articleTask.getSplitStr())[3];
						Pattern preSecondUrl = Pattern.compile(secondUrlRegex0);
						Matcher mreSecondUrl = preSecondUrl.matcher(sbPage);

						while (mreSecondUrl.find()) {
							String secondUrlTemp = getRegContent(mreSecondUrl.group(0),articleTask.getIgnoreStr(),secondUrlRegex1,secondUrlRegex2,secondUrlRegex3,articleTask.getSplitStr());
							if(null != secondUrlTemp && !"".equals(secondUrlTemp) && !articleTask.getSecondUrlList().contains(secondUrlTemp)){
								logger.info(secondUrlTemp);
								articleTask.getSecondUrlList().add(secondUrlTemp);
								TaskListClass taskListClass1 = new TaskListClass();
								taskListClass1.setArticleTask(articleTask);
								taskListClass1.setDataString(secondUrlTemp);
								QueueUtils.secondUrlQueue.add(taskListClass1);
							}
						}

						String firstUrlRegex0 = articleTask.getFirstUrlRegex().split(articleTask.getSplitStr())[0];
						String firstUrlRegex1 = articleTask.getFirstUrlRegex().split(articleTask.getSplitStr())[1];
						String firstUrlRegex2 = articleTask.getFirstUrlRegex().split(articleTask.getSplitStr())[2];
						String firstUrlRegex3 = articleTask.getFirstUrlRegex().split(articleTask.getSplitStr())[3];

						if(!firstUrlRegex0.equals(articleTask.getIgnoreStr())){
							Pattern preFirstUrl = Pattern.compile(firstUrlRegex0);
							Matcher mreFirstUrl = preFirstUrl.matcher(sbPage);

							while (mreFirstUrl.find()) {
								String indexUrlTemp = getRegContent(mreFirstUrl.group(0),articleTask.getIgnoreStr(),firstUrlRegex1,firstUrlRegex2,firstUrlRegex3,articleTask.getSplitStr());
								if(null != indexUrlTemp && !"".equals(indexUrlTemp)
										&& !articleTask.getFirstUrlList().contains(indexUrlTemp) && articleTask.getFirstUrlList().size() < articleTask.getPageSize()){
									articleTask.getFirstUrlList().add(indexUrlTemp);
									logger.info(indexUrlTemp);
									//if(firstUrlList.size()>2000) break;
									TaskListClass taskListClass2 = new TaskListClass();
									taskListClass2.setArticleTask(articleTask);
									taskListClass2.setDataString(indexUrlTemp);
									QueueUtils.indexUrlQueue.offer(taskListClass2);
									//getPageUrl(articleTask,firstUrlList,secondUrlList,splitStr,indexUrlTemp,ignoreStr,firstUrlRegex,secondUrlRegex);
								}
							}
						}
					}else{
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							logger.info(e.getMessage());
						}
					}
				}
			}
		}).start();

		//获取article
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (!QueueUtils.secondUrlQueue.isEmpty()) {
						TaskListClass taskListClass = QueueUtils.secondUrlQueue.poll();
						ArticleTask articleTask = taskListClass.getArticleTask();
						String pageUrl = taskListClass.getDataString();
						Article article = new Article();
						StringBuffer sbPage = getPageContent(articleTask.getPageCharSet(), pageUrl);
						//logger.info(sbPage);

						Pattern titlePre = Pattern.compile(articleTask.getTitleRegex().split(articleTask.getSplitStr())[0]);
						Matcher mreTitle = titlePre.matcher(sbPage);

						while (mreTitle.find()) {
							String title = getRegContent(mreTitle.group(0), articleTask.getIgnoreStr(),
									articleTask.getTitleRegex().split(articleTask.getSplitStr())[1],
									articleTask.getTitleRegex().split(articleTask.getSplitStr())[2],
									articleTask.getTitleRegex().split(articleTask.getSplitStr())[3], articleTask.getSplitStr());
							for (WordsReplace wp : BasicData.wordsReplaceList) {
								if (title.indexOf(wp.getOldWord()) != -1) {
									title = title.replace(wp.getOldWord(), wp.getNewWord());
								} else if (title.indexOf(wp.getNewWord()) != -1) {
									title = title.replace(wp.getOldWord(), wp.getNewWord());
								}
							}
							article.setTitle(title);
						}
						if (null != articleTask.getIllegalStr() && !"".equals(articleTask.getIllegalStr()) && null != article.getTitle()) {
							boolean b = false;
							String illegalStrs[] = articleTask.getIllegalStr().split(",");
							for (String s : illegalStrs) {
								if (article.getTitle().contains(s)) {
									b = true;
									break;
								}
							}
							if (b) continue;
						}

						List<Article> existA = articleService.getArticleList(article);
						if (!(null != existA && existA.size() > 0)) {
							Pattern contentPre = Pattern.compile(articleTask.getContentRegex().split(articleTask.getSplitStr())[0]);
							Matcher mreContent = contentPre.matcher(sbPage);
							while (mreContent.find()) {
								String content = getRegContent(mreContent.group(0),
										articleTask.getIgnoreStr(),
										articleTask.getContentRegex().split(articleTask.getSplitStr())[1],
										articleTask.getContentRegex().split(articleTask.getSplitStr())[2],
										articleTask.getContentRegex().split(articleTask.getSplitStr())[3], articleTask.getSplitStr());
								for (WordsReplace wp : BasicData.wordsReplaceList) {
									if (content.indexOf(wp.getOldWord()) != -1) {
										content = content.replace(wp.getOldWord(), wp.getNewWord());
									} else if (content.indexOf(wp.getNewWord()) != -1) {
										content = content.replace(wp.getOldWord(), wp.getNewWord());
									}
								}
								List<String> jpgList = getPicUrl(new StringBuffer(content));
								for (String jpg : jpgList) {
									String tempJpg = jpg;
									if (!jpg.startsWith("http")) {
										tempJpg = articleTask.getImgPre() + jpg;
									}
									String replacePath = "/" + "attachment" + "/" + sdf.format(new Date()) + "/";
									String filePath = articleTask.getPathPre() + "/" + "attachment" + "/" + sdf.format(new Date()) + "/";
									boolean b = downLoadPic(tempJpg, filePath);
									if (b) {
										int le = jpg.split("/").length;
										content = content.toString().replace(jpg, replacePath + jpg.split("/")[le - 1]);
										article.setThumbnail(replacePath + jpg.split("/")[le - 1]);
									}
									content = content.replace("<pre", "<div").replace("</pre>", "</div>");
								}
								article.setContent(content);
							}
							article.setArticleCategory(articleTask.getArticleCategory());
							try {
								article.setMeta_keywords(IKSUtil.getStringList(article.getTitle()));
							} catch (Exception e) {
								e.printStackTrace();
							}
							QueueUtils.articleQueue.offer(article);
						} else {
							if (articleTask.getRepeat() < articleTask.getMaxRepeat()) {
								logger.info("=======================================");
								logger.info(article.getTitle());
								logger.info("该文章已存在，将不再收录！！");
								logger.info("=======================================");
								articleTask.setRepeat(articleTask.getRepeat() + 1);
							} else {
								logger.info("=======================================");
								logger.info("超出重复数将停止此任务的全部录入");
								logger.info("=======================================");
								continue;
							}
						}
					} else {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							logger.info(e.getMessage());
						}
					}
				}
			}
		}).start();
		//入库
		new Thread(new Runnable() {
			public void run() {
				while(true){
					if(!QueueUtils.articleQueue.isEmpty()){
						    Article article = QueueUtils.articleQueue.poll();
							articleService.insertArticle(article);
							Integer articleId = article.getId();
							String articleCategory = article.getArticleCategory();
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
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								logger.info(e.getMessage());
							}
						}
					}
			}
		}).start();
	}

	private static List<String> getPicUrl(StringBuffer s){

		Pattern p = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic)\\b)[^>]*>", Pattern.CASE_INSENSITIVE);
		Matcher ma = p.matcher(s);
		List<String> list = new ArrayList<String>();
		String rg = "((https|http):){0,1}/{1,2}(?!(\\.jpg|\\.jpeg|\\.bmp|\\.png|\\.tif|\\.gif|\\.pcx|\\.tga|\\.exif|\\.fpx|\\.svg|\\.psd|\\.cdr|\\.pcd|\\.dxf|\\.ufo|\\.eps|\\.raw|\\.WMF|\\.webp))."
				+ "+?(\\.jpg|\\.jpeg|\\.bmp|\\.png|\\.tif|\\.gif|\\.pcx|\\.tga|\\.exif|\\.fpx|\\.svg|\\.psd|\\.cdr|\\.pcd|\\.dxf|\\.ufo|\\.eps|\\.raw|\\.WMF|\\.webp)";
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
		InputStream inputStream  = null;
		String systemName = System.getProperties().getProperty("os.name");
		try {
			File file = new File(path);
			if(!file.exists()){
				file.mkdirs();
				Runtime.getRuntime().exec("chmod 777 -R " + path);
			}
			logger.info("-----------------------------------");
			logger.info(picUrl);
			logger.info("-----------------------------------");
			URL url = new URL(picUrl);
			URLConnection urc =  url.openConnection();
			inputStream = urc.getInputStream();
			if(true){
				ByteArrayOutputStream data = new ByteArrayOutputStream();
				//设置接收附件最大20MB
				byte [] fileByte = new byte[15*1024*1024];
				int len =0;

				while((len=inputStream.read(fileByte))!=-1) {
					data.write(fileByte,0,len);
				}
				FileOutputStream fos = new FileOutputStream(path+picUrl.split("/")[(picUrl.split("/").length-1)]);
				fos.write(data.toByteArray());
				InputStream is = urc.getInputStream();
				byte [] b = new byte[1024];
				int i = 0;
				while((i = is.read(b) )!= -1) {
					fos.write(b);
				}
				fos.flush();
				//is.close();
				fos.close();
			}

		} catch (MalformedURLException e) {
			logger.info(e.getMessage());
			return false;
		} catch (IOException e) {
			logger.info(e.getMessage());
			return false;
		}
		if(false){
			String uploadfilepath =  path+picUrl.split("/")[(picUrl.split("/").length-1)];
			ChannelSftp channelsftp =  SFTPUtil.getChannelSftp();
			if(channelsftp!=null) {
				//SFTPUtil.getSFTPUtil().upload(path,uploadfilepath,channelsftp);
				SFTPUtil.getSFTPUtil().upload(path,picUrl.split("/")[(picUrl.split("/").length-1)],inputStream,channelsftp);
				//下载和删除就不写了 反正都是写一下服务器的文件路径 需要操作的文件 最后再写个channelsftp就好了
				//SFTPUtil.getSFTPUtil().disconnected(channelsftp);
			}

		}

		return true;
	}


}  
