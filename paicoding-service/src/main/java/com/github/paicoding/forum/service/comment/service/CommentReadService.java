package com.github.paicoding.forum.service.comment.service;

import com.github.paicoding.forum.api.model.vo.PageParam;
import com.github.paicoding.forum.api.model.vo.comment.dto.CommentDTO;
import com.github.paicoding.forum.api.model.vo.comment.dto.TopCommentDTO;
import com.github.paicoding.forum.service.comment.repository.entity.CommentDO;

import java.util.List;

/**
 * 评论Service接口
 *
 * @author XuYifei
 * @date 2024-07-12
 */
public interface CommentReadService {

    /**
     * 查询文章评论列表
     *
     * @param articleId
     * @param page
     * @return
     */
    List<CommentDTO> getArticleComments(Long articleId);
}
