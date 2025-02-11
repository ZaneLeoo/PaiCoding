package com.github.paicoding.forum.service.comment.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paicoding.forum.api.model.enums.YesOrNoEnum;
import com.github.paicoding.forum.service.comment.repository.entity.CommentDO;
import com.github.paicoding.forum.service.comment.repository.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentDao extends ServiceImpl<CommentMapper, CommentDO> {

    @Autowired
    private CommentMapper commentMapper;

    // 获取文章的根评论
    public List<CommentDO> getRootComments(Long articleId) {
        QueryWrapper<CommentDO> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id", articleId)
                .isNull("parent_comment_id") // 确保使用正确的列名
                .eq("deleted", YesOrNoEnum.NO.getCode());
        return commentMapper.selectList(wrapper);
    }

    // 获取文章的所有子评论
    public List<CommentDO> getAllReplies(Long articleId) {
        QueryWrapper<CommentDO> repliesWrapper = new QueryWrapper<>();
        repliesWrapper.eq("article_id", articleId)
                .isNotNull("parent_comment_id") // 排除根评论
                .eq("deleted", YesOrNoEnum.NO.getCode());
        return commentMapper.selectList(repliesWrapper);
    }

    public List<CommentDO> getAllComments(Long articleId) {
        return commentMapper.selectList(new QueryWrapper<CommentDO>().eq("article_id", articleId));
    }

    // 添加评论
    public boolean addComment(CommentDO comment) {
        return save(comment);
    }

    // 删除评论
    public boolean deleteComment(Long commentId) {
        return removeById(commentId);
    }

    // 获取所有子评论（为了删除评论时查找子评论）
    public List<CommentDO> getRepliesByParentId(Long parentCommentId) {
        QueryWrapper<CommentDO> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_comment_id", parentCommentId);
        return list(wrapper);
    }
}
