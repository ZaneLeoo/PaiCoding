package com.github.paicoding.forum.web.controller.article.vo;

import com.github.paicoding.forum.api.model.vo.comment.dto.CommentDTO;
import com.github.paicoding.forum.service.article.repository.entity.ArticleDO;
import com.github.paicoding.forum.service.comment.repository.entity.CommentDO;
import com.github.paicoding.forum.service.user.repository.entity.UserInfoDO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author XuYifei
 * @date 2024-07-12
 */
@Data
public class ArticleDetailVo {
    /**
     * 文章信息
     */
    private Long articleId;
    private String title;
    private String content;
    private Integer likes;
    private Integer commentCount;
    private Long authorId;
    private String authorName;
    private String authorAvatar;
    private String publishTime;
    private Boolean isLike;
    /**
     * 评论信息
     */
    private List<CommentDTO> comments;

}
