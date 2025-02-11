package com.github.paicoding.forum.service.article.service;

import com.github.paicoding.forum.service.article.repository.entity.ArticleContentDO;

public interface ArticleContentService {

    public ArticleContentDO getArticleContentById(Long articleId);
}
