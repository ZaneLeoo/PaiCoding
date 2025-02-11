package com.github.paicoding.forum.web.controller.comment.rest.vo;
import lombok.Data;

import java.util.List;

@Data
public class CommentVO {
    private Long id;
    private Long authorId;
    private String authorName;
    private String authorAvatar;
    private String content;
    private Long parentCommentId;
    private Integer likes;
    private List<CommentVO> replies;
}
