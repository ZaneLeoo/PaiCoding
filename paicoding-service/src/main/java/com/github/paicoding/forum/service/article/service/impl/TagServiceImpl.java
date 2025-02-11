package com.github.paicoding.forum.service.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.paicoding.forum.api.model.vo.PageParam;
import com.github.paicoding.forum.api.model.vo.PageVo;
import com.github.paicoding.forum.api.model.vo.article.dto.TagDTO;
import com.github.paicoding.forum.service.article.repository.entity.TagDO;
import com.github.paicoding.forum.service.article.repository.mapper.TagMapper;
import com.github.paicoding.forum.service.article.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标签Service
 *
 * @author XuYifei
 * @date 2024-07-12
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;


    @Override
    public List<TagDO> queryTags() {
        return tagMapper.selectList(new QueryWrapper<>());
    }

}

