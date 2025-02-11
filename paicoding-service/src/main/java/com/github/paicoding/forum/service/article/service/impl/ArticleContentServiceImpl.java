package com.github.paicoding.forum.service.article.service.impl;

import com.github.paicoding.forum.service.article.repository.dao.ArticleContentDao;
import com.github.paicoding.forum.service.article.repository.entity.ArticleContentDO;
import com.github.paicoding.forum.service.article.service.ArticleContentService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleContentServiceImpl implements ArticleContentService {
    @Autowired
    private ArticleContentDao articleContentDao;

    @Override
    public ArticleContentDO getArticleContentById(Long articleId) {
       return articleContentDao.selectContentByArticleId(articleId);
    }
}
