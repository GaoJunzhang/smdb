package com.zgj.mps.blog.dao;

import com.zgj.mps.blog.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface EsArticleDao extends ElasticsearchRepository<Article, String> {
    Page<Article> findByCategory(Integer category, Pageable pageable);
}
