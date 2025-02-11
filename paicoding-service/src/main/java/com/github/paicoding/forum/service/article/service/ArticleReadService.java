package com.github.paicoding.forum.service.article.service;

import com.github.paicoding.forum.service.article.repository.entity.ArticleContentDO;
import com.github.paicoding.forum.service.article.repository.entity.ArticleDO;
import com.github.paicoding.forum.service.article.repository.entity.ArticleItemDo;
import com.github.paicoding.forum.service.article.repository.entity.TagDO;
import java.util.List;

public interface ArticleReadService {

    /**
     * 根据文章ID获取文章
     */
    public ArticleDO getArticleById(Long id);

    /**
     * 获取TOP文章
     *
     * @return
     */
    List<ArticleItemDo> getTopArticleItems(int top);

    /**
     * 查询文章标签列表
     *
     * @param articleId
     * @return
     */
    List<TagDO> queryTagsByArticleId(Long articleId);

    /**
     * 查询文章正文内容
     *
     * @param articleId
     * @return
     */
    ArticleContentDO queryContentArticleInfo(Long articleId);


    /**
     * 查询某个分类下的文章
     *
     * @param categoryId
     * @return
     */
    List<ArticleItemDo> queryArticlesByTag(Long tagId);



    /**
     * 根据查询条件查询文章列表
     *
     * @param key
     * @return
     */
    List<ArticleDO> queryArticlesBySearchKey(String key);

    /**
     * 查询用户的文章列表
     *
     * @param userId
     * @return
     */
    List<ArticleDO> queryArticlesByUserId(Long userId);

    /**
     * 查询作者的文章数
     *
     * @param authorId
     * @return
     */
    int queryArticleCount(long authorId);

    /**
     * 返回总的文章计数
     *
     * @return
     */
    Long getArticleCount();
}
