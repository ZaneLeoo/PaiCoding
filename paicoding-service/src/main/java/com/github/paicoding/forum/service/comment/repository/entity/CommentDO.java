package com.github.paicoding.forum.service.comment.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@TableName("comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long articleId;
    private Long userId;
    private String content;
    private Long parentCommentId;
    private Integer likes;
    private Integer deleted;

    @TableField(exist = false)
    private List<CommentDO> replies;  // 子评论
}
