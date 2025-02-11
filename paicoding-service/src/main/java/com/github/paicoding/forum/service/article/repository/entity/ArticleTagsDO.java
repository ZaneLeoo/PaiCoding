package com.github.paicoding.forum.service.article.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章和标签的关联表实体类，映射到 article_tag 表
 */
@Data
@NoArgsConstructor
@TableName("article_tag")
public class ArticleTagsDO {

    /**
     * 文章 ID，外键关联 article 表
     */
    private Long articleId;

    /**
     * 标签 ID，外键关联 tags 表
     */
    private Long tagId;

}
