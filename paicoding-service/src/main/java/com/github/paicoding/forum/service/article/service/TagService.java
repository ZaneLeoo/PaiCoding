package com.github.paicoding.forum.service.article.service;

import com.github.paicoding.forum.api.model.vo.PageParam;
import com.github.paicoding.forum.api.model.vo.PageVo;
import com.github.paicoding.forum.api.model.vo.article.dto.TagDTO;
import com.github.paicoding.forum.service.article.repository.entity.TagDO;

import java.util.List;

/**
 * 标签Service
 *
 * @author XuYifei
 * @date 2024-07-12
 */
public interface TagService {

    List<TagDO> queryTags();

}
