package com.github.paicoding.forum.web.controller.search.rest;

import com.github.paicoding.forum.api.model.vo.ResVo;
import com.github.paicoding.forum.api.model.vo.article.dto.SimpleArticleDTO;
import com.github.paicoding.forum.service.article.repository.entity.ArticleDO;
import com.github.paicoding.forum.service.article.service.ArticleReadService;
import com.github.paicoding.forum.web.controller.search.vo.SearchArticleVo;
import com.github.paicoding.forum.web.global.BaseViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 推荐服务接口
 *
 * @author XuYifei
 * @date 2024-07-12
 */
@RequestMapping(path = "search/api")
@RestController
public class SearchRestController extends BaseViewController {

    @Autowired
    private ArticleReadService articleReadService;

    /**
     * 根据关键词给出搜索下拉框
     *
     * @param key
     */
//    @GetMapping(path = "hint")
//    public ResVo<SearchArticleVo> recommend(@RequestParam(name = "key", required = false) String key) {
//        List<ArticleDO> list = articleReadService.queryArticlesBySearchKey(key);
//        SearchArticleVo vo = new SearchArticleVo();
//        vo.setKey(key);
//        vo.setItems(list);
//        return ResVo.ok(vo);
//    }

}
