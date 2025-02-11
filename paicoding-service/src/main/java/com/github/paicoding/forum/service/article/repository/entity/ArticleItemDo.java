package com.github.paicoding.forum.service.article.repository.entity;

import com.github.paicoding.forum.api.model.vo.article.dto.TagDTO;
import com.github.paicoding.forum.service.user.repository.entity.UserInfoDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleItemDo {

    Long articleId;
    String articleTitle;
    UserInfoDO articleAuthor;
    String articleSummary;
    List<String> articleTags;
    Integer articleLikeCount;
    Integer articleCommentCount;
    LocalDateTime articleCreateTime;
    String articleCoverUrl;
}
