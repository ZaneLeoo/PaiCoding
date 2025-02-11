package com.github.paicoding.forum.service.article.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paicoding.forum.service.article.repository.entity.ArticleTagsDO;
import com.github.paicoding.forum.service.article.repository.mapper.ArticleTagsMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleTagsDao extends ServiceImpl<ArticleTagsMapper, ArticleTagsDO> {
}
