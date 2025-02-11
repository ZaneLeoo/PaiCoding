package com.github.paicoding.forum.service.comment.service.impl;

import com.github.paicoding.forum.api.model.enums.NotifyTypeEnum;
import com.github.paicoding.forum.api.model.enums.YesOrNoEnum;
import com.github.paicoding.forum.api.model.exception.ExceptionUtil;
import com.github.paicoding.forum.api.model.vo.constants.StatusEnum;
import com.github.paicoding.forum.service.article.repository.entity.ArticleDO;
import com.github.paicoding.forum.service.article.service.ArticleReadService;
import com.github.paicoding.forum.service.comment.repository.dao.CommentDao;
import com.github.paicoding.forum.service.comment.repository.entity.CommentDO;
import com.github.paicoding.forum.service.comment.service.CommentWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


/**
 * 评论Service
 *
 * @author XuYifei
 * @date 2024-07-12
 */
@Service
public class CommentWriteServiceImpl implements CommentWriteService {

    @Autowired
    private CommentDao commentDao;
    @Autowired
    private ArticleReadService articleReadService;

    @Override
    public boolean addComment(CommentDO comment) {

        // 1. 校验评论内容，检查是否为空、恶意内容等
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("评论内容不能为空");
        }
        // 2. 如果是子评论，确保 parent_comment_id 不为空
        if (comment.getParentCommentId() != null && comment.getParentCommentId() > 0) {
            // 子评论，校验父评论是否存在
            CommentDO parentComment = commentDao.getById(comment.getParentCommentId());
            if (parentComment == null) {
                throw new IllegalArgumentException("父评论不存在");
            }
        } else {
            // 根评论，parent_comment_id 为 null 或 0
            comment.setParentCommentId(null);  // 或 comment.setParentCommentId(0L);
        }
        // 3. 调用 DAO 层保存评论
        return commentDao.addComment(comment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean  deleteComment(Long commentId) {
        // 1. 检查该评论是否存在
        CommentDO comment = commentDao.getById(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("评论不存在");
        }
        // 校验文章信息
        ArticleDO article = articleReadService.getArticleById(comment.getArticleId());
        if (article == null) {
            throw ExceptionUtil.of(StatusEnum.ARTICLE_NOT_EXISTS, comment.getArticleId());
        }
        // 2. 获取该评论的子评论
        List<CommentDO> replies = commentDao.getRepliesByParentId(commentId);

        // 3. 如果有子评论，可以选择删除子评论，或者递归删除
        if (replies != null && !replies.isEmpty()) {
            // 递归删除所有子评论
            replies.forEach(reply -> deleteComment(reply.getId()));
        }
        // 4. 删除当前评论
        return commentDao.deleteComment(commentId);
    }
}
