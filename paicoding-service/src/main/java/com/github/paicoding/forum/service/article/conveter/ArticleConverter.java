package com.github.paicoding.forum.service.article.conveter;

import com.github.paicoding.forum.api.model.vo.article.ArticlePostReq;
import com.github.paicoding.forum.service.article.repository.entity.ArticleDO;
import com.github.paicoding.forum.service.article.repository.entity.ArticleTagsDO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 文章转换
 * <p>
 *
 * @author XuYifei
 * @date 2024-07-12
 */
public class ArticleConverter {

   public static ArticleDO toNewArticleDO(ArticlePostReq articlePostReq,Long authorId) {
       ArticleDO article = new ArticleDO();
       article.setId(null);
       article.setUserId(2L);
       article.setId(articlePostReq.getArticleId());
       article.setTitle(articlePostReq.getTitle());
       article.setSummary(articlePostReq.getSummary());
       article.setLikes(0);
       article.setViews(0);
       article.setComments(0);
       article.setCover(articlePostReq.getCover());
       article.setDeleted(false);
       article.setStatus(articlePostReq.getStatus());
       return article;
   }

   public static List<ArticleTagsDO> toArticleTagsDOList(Set<Long> tags,Long authorId) {
       ArrayList<ArticleTagsDO> list = new ArrayList<>();
       tags.forEach(tag -> {
           ArticleTagsDO articleTagsDO = new ArticleTagsDO();
           articleTagsDO.setTagId(tag);
           articleTagsDO.setArticleId(authorId);
           list.add(articleTagsDO);
       });
       return list;
   }
}
