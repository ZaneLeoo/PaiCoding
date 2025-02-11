package com.github.paicoding.forum.service.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.github.paicoding.forum.api.model.vo.article.dto.ArticleDTO;
import com.github.paicoding.forum.service.article.repository.dao.ArticleContentDao;
import com.github.paicoding.forum.service.article.repository.dao.ArticleDao;
import com.github.paicoding.forum.service.article.repository.dao.ArticleTagsDao;
import com.github.paicoding.forum.service.article.repository.entity.*;
import com.github.paicoding.forum.service.article.repository.mapper.ArticleTagsMapper;
import com.github.paicoding.forum.service.article.repository.mapper.TagMapper;
import com.github.paicoding.forum.service.article.service.ArticleReadService;
import com.github.paicoding.forum.service.comment.repository.entity.CommentDO;
import com.github.paicoding.forum.service.comment.service.impl.CommentReadServiceImpl;
import com.github.paicoding.forum.service.user.repository.entity.UserInfoDO;
import com.github.paicoding.forum.service.user.repository.mapper.UserInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;

/**
 * 文章查询相关服务类
 *
 * @author XuYifei
 * @date 2024-07-09
 */
@Slf4j
@Service
public class ArticleReadServiceImpl implements ArticleReadService {

    private UserInfoMapper userInfoMapper;
    private ArticleDao articleDao;
    private ArticleTagsMapper articleTagsMapper;
    private ArticleContentDao articleContentDao;
    private TagMapper tagMapper;
    private CommentReadServiceImpl commentReadService;
    private ArticleTagsDao articleTagsDao;

    @Override
    public ArticleDO getArticleById(Long id) {
        return articleDao.getById(id);
    }

    @Override
    public List<ArticleItemDo> getTopArticleItems(int top) {

        // 查询文章信息
        List<ArticleDO> topArticles = articleDao.getArticlesByViews(top);
        // 查询文章作者信息
        HashSet<Long> authorIds = new HashSet<>();
        topArticles.forEach(articleDO -> {
            authorIds.add(articleDO.getUserId());
        });
        List<UserInfoDO> authors;
        if (authorIds != null && !authorIds.isEmpty()) {
            authors = userInfoMapper.selectBatchIds(authorIds);
        } else {
            return null;
        }

        // 填充 ArticleItemDo 值并返回
        ArrayList<ArticleItemDo> items = new ArrayList<>();
        topArticles.forEach(articleDO -> {
            ArticleItemDo item = new ArticleItemDo();
            List<String> tags = queryTagsByArticleId(articleDO.getId()).stream().map(TagDO::getName).toList();
            UserInfoDO author = authors.stream().
                    filter(u -> u.getId().equals(articleDO.getUserId())).findFirst().get();
            item.setArticleId(articleDO.getId());
            item.setArticleTitle(articleDO.getTitle());
            item.setArticleSummary(articleDO.getSummary());
            item.setArticleTags(tags);
            item.setArticleLikeCount(articleDO.getLikes());
            item.setArticleCommentCount(articleDO.getComments());
            item.setArticleCreateTime(articleDO.getCreateTime());
            item.setArticleCoverUrl(articleDO.getCover());
            item.setArticleAuthor(author);
            items.add(item);
        });
        return items;
    }

    @Override
    public List<TagDO> queryTagsByArticleId(Long articleId) {
        QueryWrapper<ArticleTagsDO> wrapper = new QueryWrapper<ArticleTagsDO>().eq("article_id", articleId);
        List<Long> tagIds = articleTagsMapper.selectList(wrapper).stream().map(ArticleTagsDO::getTagId).toList();
        return tagMapper.selectBatchIds(tagIds);
    }

    @Override
    public ArticleContentDO queryContentArticleInfo(Long articleId) {
        return articleContentDao.selectContentByArticleId(articleId);
    }


    @Override
    public List<ArticleItemDo> queryArticlesByTag(Long tagId) {
        QueryWrapper<ArticleTagsDO> wrapper = new QueryWrapper<>();
        wrapper.eq("tag_id", tagId);
        List<ArticleTagsDO> list = articleTagsDao.list(wrapper);
        List<Long> ids = list.stream().map(ArticleTagsDO::getArticleId).toList();
        List<ArticleDO> articles = articleDao.listByIds(ids);
        HashSet<Long> authorIds = new HashSet<>();
        articles.forEach(articleDO -> {
            authorIds.add(articleDO.getUserId());
        });
        List<UserInfoDO> authors;
        if (authorIds != null && !authorIds.isEmpty()) {
            authors = userInfoMapper.selectBatchIds(authorIds);
        } else {
            return null;
        }
        // 填充 ArticleItemDo 值并返回
        ArrayList<ArticleItemDo> items = new ArrayList<>();
        articles.forEach(articleDO -> {
            ArticleItemDo item = new ArticleItemDo();
            List<String> tags = queryTagsByArticleId(articleDO.getId()).stream().map(TagDO::getName).toList();
            UserInfoDO author = authors.stream().
                    filter(u -> u.getId().equals(articleDO.getUserId())).findFirst().get();
            item.setArticleId(articleDO.getId());
            item.setArticleTitle(articleDO.getTitle());
            item.setArticleSummary(articleDO.getSummary());
            item.setArticleTags(tags);
            item.setArticleLikeCount(articleDO.getLikes());
            item.setArticleCommentCount(articleDO.getComments());
            item.setArticleCreateTime(articleDO.getCreateTime());
            item.setArticleCoverUrl(articleDO.getCover());
            item.setArticleAuthor(author);
            items.add(item);
        });
        return items;
    }

    @Override
    public List<ArticleDO> queryArticlesBySearchKey(String key) {
        LambdaQueryChainWrapper<ArticleDO> result = articleDao.lambdaQuery().like(ArticleDO::getTitle, key);
        return result.list();
    }

    @Override
    public List<ArticleDO> queryArticlesByUserId(Long userId) {
        return articleDao.getArticlesByUserId(userId);
    }

    @Override
    public int queryArticleCount(long authorId) {
        QueryWrapper<ArticleDO> wrapper = new QueryWrapper<>();
        wrapper.eq("author_id", authorId);
        long count = articleDao.count(wrapper);
        return (int) count;
    }

    @Override
    public Long getArticleCount() {
        return articleDao.count();
    }

    @Autowired
    public void setArticleContentDao(ArticleContentDao articleContentDao) {
        this.articleContentDao = articleContentDao;
    }

    @Autowired
    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    @Autowired
    public void setArticleTagsMapper(ArticleTagsMapper articleTagsMapper) {
        this.articleTagsMapper = articleTagsMapper;
    }

    @Autowired
    public void setUserInfoMapper(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    @Autowired
    public void setTagMapper(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Autowired
    public void setCommentReadService(CommentReadServiceImpl commentReadService) {
        this.commentReadService = commentReadService;
    }

    @Autowired
    public void setArticleTagsDao(ArticleTagsDao articleTagsDao) {
        this.articleTagsDao = articleTagsDao;
    }


}
