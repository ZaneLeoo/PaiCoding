package com.github.paicoding.forum.service.article.repository.entity;



import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文章实体类，映射到 article 表
 */
@Data
@NoArgsConstructor
@TableName("article")
public class ArticleDO {

    /**
     * 文章 ID，主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户 ID，关联到用户表
     */
    private Long userId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 文章点赞数
     */
    private Integer likes;

    /**
     * 文章浏览数
     */
    private Integer views;

    /**
     * 文章评论数量
     */
    private Integer comments;

    /**
     * 文章发布状态
     * 0 未发布
     * 1 已发布
     */
    private Integer status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 文章封面图 URL
     */
    private String cover;

    /**
     * 文章是否已删除
     */
    private Boolean deleted;

}
