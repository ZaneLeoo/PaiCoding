package com.github.paicoding.forum.api.model.vo.comment.dto;

import lombok.Data;

import java.util.List;
@Data
public class CommentDTO {
    private Long id;
    private Long authorId;
    private String authorName;
    private String authorAvatar;
    private String content;
    private Long parentCommentId;
    private Integer likes;
    private List<CommentDTO> replies;
}
