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
        List<String> s = new ArrayList<String>();
        while ((lex = ik.next()) != null) {
            s.add(lex.getLexemeText().toString());
        }
        String ary [] = (String [])s.toArray();
        String str1= StringUtils.join(ary, ",");
        return str1;
    }

}
