package com.github.paicoding.forum.web.controller.article.vo;

import lombok.Data;

@Data
public class ArticleStatusVo {

    private Boolean isLike;
    private Integer likeCount;
    private Integer commentCount;
}
