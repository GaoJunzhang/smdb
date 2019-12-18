package com.zgj.mps.blog.service;

import com.zgj.mps.blog.model.Article;
import com.zgj.mps.vo.SearchVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EsArticleService {
    Page<Article> findByCategory(Integer type, Pageable pageable);

    Article saveArticle(Article article);

    void deleteArticle(String id);

    void deleteAllArticle();

    Page<Article> findByConfition(Integer type, String key, SearchVo searchVo, Pageable pageable);
}
