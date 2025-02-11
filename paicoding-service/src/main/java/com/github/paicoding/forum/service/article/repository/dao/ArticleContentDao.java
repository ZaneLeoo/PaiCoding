package com.github.paicoding.forum.service.article.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paicoding.forum.service.article.repository.entity.ArticleContentDO;
import com.github.paicoding.forum.service.article.repository.mapper.ArticleContentMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleContentDao extends ServiceImpl<ArticleContentMapper, ArticleContentDO> {

    public ArticleContentDO selectContentByArticleId(Long articleId){
        // 使用 ServiceImpl 提供的 getById 方法
        return getById(articleId);
    }
}
