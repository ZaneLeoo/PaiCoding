package com.github.paicoding.forum.service.article.repository.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.paicoding.forum.service.article.repository.entity.ArticleDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Article 表的 Mapper 接口
 */

public interface ArticleMapper extends BaseMapper<ArticleDO> {
    
}
