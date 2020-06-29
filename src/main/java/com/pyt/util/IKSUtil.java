package com.pyt.util;

import org.apache.commons.lang.StringUtils;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 2020/6/27.
 */
public class IKSUtil {

    public static String getStringList(String text) throws Exception{
        //独立Lucene实现
        StringReader re = new StringReader(text);
        IKSegmenter ik = new IKSegmenter(re, true);
        Lexeme lex;
        String str1 = "";
        while ((lex = ik.next()) != null) {
            str1 += lex.getLexemeText() +",";
        }
        str1.substring(0,str1.length()-2);
        return str1;
    }
    public static void  main(String [] args){
        try{
            System.out.println(getStringList("我还是那个模样"));
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
