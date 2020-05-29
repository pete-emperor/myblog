package com.pyt.dao;

import com.pyt.bean.Article;
import com.pyt.bean.ArticleCategoryMapping;
import com.pyt.bean.Blog;
import com.pyt.bean.WordsReplace;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by peter on 2020/4/2.
 */
@Mapper

@Component
public interface ArticleDao {
    public List<Article> getArticleList(Article article);
    public Integer insertArticle(Article article);
    public void insertArCaMa(ArticleCategoryMapping arCaMa);
    public Integer getArticleCount(Article article);
    public List<WordsReplace> getWordsReplaceList(WordsReplace wordsReplace);
}
