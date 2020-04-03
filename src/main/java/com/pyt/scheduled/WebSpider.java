package com.pyt.scheduled;

import com.pyt.MyblogApplication;
import com.pyt.bean.Blog;
import com.pyt.bean.Task;
import com.pyt.service.BlogService;
import com.pyt.util.QueueUtils;
import com.pyt.util.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

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
public class WebSpider implements ApplicationRunner {

	private BlogService blogService = SpringUtils.getBean(BlogService.class);

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		while (true) {
			if (!QueueUtils.taskQueue.isEmpty()) {
				Task task = QueueUtils.taskQueue.poll();
				this.ScanSpider(task);
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void ScanSpider(Task task) {
		String urlStr = task.getUrl();
		URLConnection urlconn = null;
		BufferedReader br = null;
		Map urlMap = new HashMap();
		PrintWriter pw = null;
		String regex = urlStr + "/article/details/\\d{8,}";
		String regexe = "<div class=\"blog-content-box\">[\\s\\S]*</article>";
		String regexee = "<link rel=\"stylesheet\" [\\s\\S]*.css\">";
		String regexcss = "<style[\\s\\S]*</style>";
		String regexes = "<span class=\"article-type type-(1|2) float-none\">[\\s\\SS]*</a>";
		Pattern pree = Pattern.compile(regexes);
		Pattern p = Pattern.compile(regex);
		try {
			URL url = new URL(urlStr);
			urlconn = url.openConnection();
			br = new BufferedReader(new InputStreamReader(urlconn.getInputStream(), "UTF-8"));
			String buf = null;
			List<String> titleList = new ArrayList<String>();
			List<String> urlList = new ArrayList<String>();
			while ((buf = br.readLine()) != null) {
				String title = "";
				String pageHtml = "";
				Matcher mree = pree.matcher(buf);
				while (mree.find()) {
					title = mree.group(0).substring(mree.group(0).lastIndexOf("</span>") + 7, mree.group(0).lastIndexOf("</a>"));
					titleList.add(title);
				}

				Pattern p2e = Pattern.compile(regex);
				Matcher m2e = p2e.matcher(buf);
				while (m2e.find()) {
					if (!urlMap.containsKey(m2e.group(0))) {

						try {
							urlMap.put(m2e.group(0), m2e.group(0));
							pageHtml = m2e.group(0).substring(m2e.group(0).lastIndexOf('/') + 1, m2e.group(0).length()) + ".html";
							String path = ClassUtils.getDefaultClassLoader().getResource("views/page").getPath();
							path = path.substring(1, path.length());
							//String path = "F:\\spider\\page";
							String fileName = path + "/" + pageHtml;
							urlList.add(pageHtml);
							URL urle = new URL(m2e.group(0));
							URLConnection urlconne = urle.openConnection();
							BufferedReader bre = new BufferedReader(new InputStreamReader(urlconne.getInputStream(), "UTF-8"));
							String bufe = null;
							String bufee = "";
							PrintWriter pw2 = new PrintWriter(new FileWriter(fileName), true);
							pw2.println("<html>");
							pw2.println("<head>");

							while ((bufe = bre.readLine()) != null) {
								Pattern pre1 = Pattern.compile(regexee);
								Matcher mre1 = pre1.matcher(bufe);
								if (mre1.find()) {
									pw2.println(mre1.group(0));
								}
								bufee += "\n" + bufe;
								Pattern pre = Pattern.compile(regexe);
								Matcher mre = pre.matcher(bufee);
								if (mre.find()) {
									Pattern precss1 = Pattern.compile(regexcss);
									Matcher mrecss1 = precss1.matcher(bufee);
									while (mrecss1.find()) {
										pw2.println(mrecss1.group(0));
									}
									pw2.println("</head>");
									pw2.println("<body>");
									pw2.println("<div style=\"width:852px;margin:0 auto;\">");
									pw2.println(mre.group(0).replace("src=\"//", "src=\"https://"));
									pw2.println("</div>");
									break;
								}
								;
							}
							pw2.println("</div>");
							pw2.println("</body>");
							pw2.println("</html>");
							pw2.close();
							bre.close();


						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			}
			for (int i = 0; i < titleList.size(); i++) {
				if (urlList.size() > i && null != urlList.get(i)) {
					Blog blog = new Blog();
					blog.setTitle(titleList.get(i));
					blog.setUrl(urlList.get(i));
					blogService.insertBlog(blog);
				}
			}

			System.out.println("task结束");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


}  
