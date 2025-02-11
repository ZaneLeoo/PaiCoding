package com.github.paicoding.forum.web.controller.tag.rest;
import com.github.paicoding.forum.service.article.repository.entity.TagDO;
import com.github.paicoding.forum.service.article.service.TagService;
import com.github.paicoding.forum.web.global.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: tech-pai
 * @description:
 * @author: XuYifei
 * @create: 2024-07-05
 */

@RestController
@RequestMapping("/api/tag")
public class TagRestController {
    @Autowired
    private TagService tagService;

    @GetMapping("/all")
    public ResultVo<List<TagDO>> listAll(){
        return ResultVo.ok(tagService.queryTags());
    }
}
