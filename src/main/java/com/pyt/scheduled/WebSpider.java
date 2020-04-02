package com.pyt.scheduled;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;  
  
public class WebSpider {  
    public void run() {
    	/*String ss ="var baseUrl = 'https://blog.csdn.ne22221111t/weixin_43901866/article/list' ;";
    	String rf1 ="https://blog.csdn.ne22221111t/weixin_\\d{8}";
    	Pattern p2 = Pattern.compile(rf1);
    	Matcher m2= p2.matcher(ss);
    	while(m2.find())
    	{
    		System.out.println(m2.group(0));
    	}
    	String rf ="1[3|4|5|8][0-9]\\d{8}";
    	
    	String num = "wefewfwef183178974285fff18317897456efewf";
    	Pattern p1 = Pattern.compile(rf);
    	Matcher m1= p1.matcher(num);
    	while(m1.find())
    	{
    		System.out.println(m1.group(0));
    	}*/
    	
    	
    	
        URL url = null;  
        URLConnection urlconn = null;  
        BufferedReader br = null;  
        Map urlMap = new HashMap();
        PrintWriter pw = null;  //https://blog.csdn.net/weixin_43901866/article/details
        String regex = "https://blog.csdn.net/weixin_43901866/article/details/\\d{8,}";  
        String regexe = "<div class=\"blog-content-box\">[\\s\\S]*</article>";  
        String regexee = "<link rel=\"stylesheet\" [\\s\\S]*.css\">";  
        String regexcss = "<style[\\s\\S]*</style>";  
        Pattern p = Pattern.compile(regex);  
        try {  
            url = new URL("https://blog.csdn.net/weixin_43901866");  
            urlconn = url.openConnection();  
            pw = new PrintWriter(new FileWriter("D:/spider/url.txt"), true);//�������ǰ��ռ��������Ӵ洢����E�̵��µ�һ������url��txt�ļ���
            br = new BufferedReader(new InputStreamReader(  
                    urlconn.getInputStream(),"UTF-8"));  
            String buf = null;  
            while ((buf = br.readLine()) != null) {  
            	Pattern p2e = Pattern.compile(regex);
            	Matcher m2e= p2e.matcher(buf);
            	while(m2e.find())
            	{
            		if(!urlMap.containsKey(m2e.group(0))){
            			
            			try {
            				urlMap.put(m2e.group(0), m2e.group(0));
   	               		 //System.out.println(m2e.group(0));
   	               		 pw.println(m2e.group(0)); 
   	               		 String fileName = "D:/spider/page/"+m2e.group(0).substring(m2e.group(0).lastIndexOf('/'), m2e.group(0).length())+".html";
   	               		 
   	               		 URL urle = new URL(m2e.group(0));  
   	               		 URLConnection urlconne = urle.openConnection();  
   	               		 BufferedReader bre = new BufferedReader(new InputStreamReader(  
   	               				urlconne.getInputStream(),"UTF-8"));  
   	               		 String bufe = null;  
   	               		 String bufee = "";  
   	               		 PrintWriter pw2 = new PrintWriter(new FileWriter(fileName), true);
   	               		 pw2.println("<html>");
   	               		 pw2.println("<head>");
   	               		 
   	                     while ((bufe = bre.readLine()) != null) { 
   	                    	 
   	                    	 Pattern pre1 = Pattern.compile(regexee);
   		               		 Matcher mre1 = pre1.matcher(bufe);
   		               		 if(mre1.find()){
   		               			 pw2.println(mre1.group(0));
   		               		 }
   		               		 
   	                    	 bufee += "\n" + bufe;
   		               		 
   	                    	 Pattern pre = Pattern.compile(regexe);
   		               		 Matcher mre = pre.matcher(bufee);
   		               		 if(mre.find()){
   		               			 Pattern precss1 = Pattern.compile(regexcss);
   			               		 Matcher mrecss1 = precss1.matcher(bufee);
   			               		 while(mrecss1.find()){
   			               			 pw2.println(mrecss1.group(0));
   			               		 }
   		               			 pw2.println("</head>");
   			               		 pw2.println("<body>");
   			               		 pw2.println("<div style=\"width:852px;margin:0 auto;\">");
   		               			 pw2.println(mre.group(0).replace("src=\"//", "src=\"https://"));
   		               			 pw2.println("</div>");
   		               			 break;
   		               		 };
   		               		 
   	                     }
   	               		 pw2.println("</div>");
   	               		 pw2.println("</body>");
   	               		 pw2.println("</html>");
   	               		 pw2.close();
   	               		 bre.close(); 
            			}catch (Exception e) {
							// TODO: handle exception
						}
            			  
	               		
            		}
            		  
            	}
            }  
            System.out.println("��ȡ�ɹ���");  
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
            pw.close();  
        }  
    }  
}  
