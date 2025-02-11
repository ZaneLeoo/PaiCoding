package com.github.paicoding.forum.service.article.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.paicoding.forum.service.article.repository.entity.ArticleDO;
import com.github.paicoding.forum.service.article.repository.entity.ArticleTagsDO;
import com.github.paicoding.forum.service.article.repository.entity.TagDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章标签映mapper接口
 *
 * @author XuYifei
 * @date 2024-07-12
 */
public interface ArticleTagsMapper extends BaseMapper<ArticleTagsDO> {

}
