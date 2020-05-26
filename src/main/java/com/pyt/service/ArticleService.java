package com.pyt.service;

import com.pyt.bean.Article;
import com.pyt.bean.Article;
import com.pyt.bean.WordsReplace;
import com.pyt.dao.ArticleDao;
import com.pyt.dao.ArticleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by PC on 2020/4/1.
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    public List<Article> getArticleList(Article article){
        return articleDao.getArticleList(article);
    }
    public Integer getArticleCount(Article article){
        return articleDao.getArticleCount(article);
    }
    public void insertArticle(Article article){
        articleDao.insertArticle(article);
    }
    public List<WordsReplace> getWordsReplaceList(WordsReplace wordsReplace){
        return articleDao.getWordsReplaceList(wordsReplace);
    }
}
