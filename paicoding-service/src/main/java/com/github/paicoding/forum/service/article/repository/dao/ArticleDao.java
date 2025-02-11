package com.github.paicoding.forum.service.article.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paicoding.forum.service.article.repository.entity.ArticleDO;
import com.github.paicoding.forum.service.article.repository.entity.ArticleTagsDO;
import com.github.paicoding.forum.service.article.repository.mapper.ArticleMapper;
import com.github.paicoding.forum.service.article.repository.mapper.ArticleTagsMapper;
import com.github.paicoding.forum.service.article.repository.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * 文章相关DB操作
 * <p>
 * 多表结构的操作封装，只与DB操作相关
 *
 * @author XuYifei
 * @date 2024-07-12
 */
@Repository
public class ArticleDao extends ServiceImpl<ArticleMapper, ArticleDO> {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleTagsMapper articleTagsMapper;
    @Autowired
    private TagMapper tagMapper;

    /**
     * 根据文章 ID 获取文章详情
     * @param articleId 文章 ID
     * @return 文章对象
     */
    public ArticleDO getArticleById(Long articleId) {
        return articleMapper.selectById(articleId);
    }

    /**
     * 获取指定用户的文章列表
     * @param userId 用户 ID
     * @return 文章列表
     */
    public List<ArticleDO> getArticlesByUserId(Long userId) {
        QueryWrapper<ArticleDO> wrapper = new QueryWrapper<ArticleDO>().eq("user_id", userId);
        return articleMapper.selectList(wrapper);
    }

    /** todo
     * 获取指定标签的文章列表
     * @param tagId 标签 ID
     * @return 文章列表
     */
    public List<ArticleDO> getArticlesByTagId(Long tagId) {
        QueryWrapper<ArticleTagsDO> wrapper = new QueryWrapper<ArticleTagsDO>().eq("tag_id", tagId);
        List<Long> list = articleTagsMapper.selectList(wrapper).stream().map(ArticleTagsDO::getArticleId).toList();
        return articleMapper.selectBatchIds(list);
    }

    /**
     * 根据标题模糊查询文章
     * @param title 文章标题
     * @return 文章列表
     */
    public List<ArticleDO> getArticlesByTitle(String title) {
        QueryWrapper<ArticleDO> wrapper = new QueryWrapper<ArticleDO>().like("title", title);
        return articleMapper.selectList(wrapper);
    }

    /**
     * 添加新的文章
     * @param article 文章实体
     * @return 是否成功
     */
    public boolean addArticle(ArticleDO article) {
        return this.save(article);
    }

    /**
     * 更新文章信息
     * @param article 文章实体
     * @return 是否成功
     */
    public boolean updateArticle(ArticleDO article) {
        return this.updateById(article);
    }

    /**
     * 删除指定文章
     * @param articleId 文章 ID
     * @return 是否成功
     */
    public boolean deleteArticle(Long articleId) {
        return this.removeById(articleId);
    }

    /**
     * 根据点赞数降序获取文章
     * @param limit 获取的文章数量限制
     * @return 文章列表
     */
    public List<ArticleDO> getArticlesByLikes(int limit) {
        QueryWrapper<ArticleDO> wrapper = new QueryWrapper<ArticleDO>().orderByDesc("likes");
        return  articleMapper.selectList(wrapper);
    }

    /**
     * 根据浏览量降序获取文章
     * @param limit 获取的文章数量限制
     * @return 文章列表
     */
    public List<ArticleDO> getArticlesByViews(int limit) {
        QueryWrapper<ArticleDO> wrapper = new QueryWrapper<ArticleDO>().orderByDesc("views");
        return  articleMapper.selectList(wrapper);
    }
}