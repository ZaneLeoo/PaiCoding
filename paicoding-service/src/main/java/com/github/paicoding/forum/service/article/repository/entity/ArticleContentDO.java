package com.github.paicoding.forum.service.article.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("article_content")
public class ArticleContentDO {
    @TableId
    private Long articleId;
    private String content;
}
