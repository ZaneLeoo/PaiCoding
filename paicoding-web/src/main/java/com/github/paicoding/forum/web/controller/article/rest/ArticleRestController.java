package com.github.paicoding.forum.web.controller.article.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.paicoding.forum.api.model.context.ReqInfoContext;
import com.github.paicoding.forum.api.model.enums.DocumentTypeEnum;
import com.github.paicoding.forum.api.model.enums.NotifyTypeEnum;
import com.github.paicoding.forum.api.model.enums.OperateTypeEnum;
import com.github.paicoding.forum.api.model.enums.article.ArticleTagEnum;
import com.github.paicoding.forum.api.model.event.MessageQueueEvent;
import com.github.paicoding.forum.api.model.vo.PageParam;
import com.github.paicoding.forum.api.model.vo.PageVo;
import com.github.paicoding.forum.api.model.vo.ResVo;
import com.github.paicoding.forum.api.model.vo.article.ArticlePostReq;
import com.github.paicoding.forum.api.model.vo.article.ContentPostReq;
import com.github.paicoding.forum.api.model.vo.article.dto.ArticleDTO;
import com.github.paicoding.forum.api.model.vo.article.dto.ArticleOtherDTO;
import com.github.paicoding.forum.api.model.vo.article.dto.CategoryDTO;
import com.github.paicoding.forum.api.model.vo.article.dto.TagDTO;
import com.github.paicoding.forum.api.model.vo.article.response.CategoryArticlesResponseDTO;
import com.github.paicoding.forum.api.model.vo.comment.dto.CommentDTO;
import com.github.paicoding.forum.api.model.vo.comment.dto.TopCommentDTO;
import com.github.paicoding.forum.api.model.vo.constants.StatusEnum;
import com.github.paicoding.forum.api.model.vo.notify.NotifyMsgEvent;
import com.github.paicoding.forum.api.model.vo.recommend.SideBarDTO;
import com.github.paicoding.forum.api.model.vo.user.dto.UserStatisticInfoDTO;
import com.github.paicoding.forum.core.common.CommonConstants;
import com.github.paicoding.forum.core.permission.Permission;
import com.github.paicoding.forum.core.permission.UserRole;
import com.github.paicoding.forum.core.util.SpringUtil;
import com.github.paicoding.forum.service.article.repository.dao.ArticleContentDao;
import com.github.paicoding.forum.service.article.repository.dao.ArticleTagsDao;
import com.github.paicoding.forum.service.article.repository.entity.ArticleContentDO;
import com.github.paicoding.forum.service.article.repository.entity.ArticleDO;
import com.github.paicoding.forum.service.article.repository.entity.ArticleItemDo;
import com.github.paicoding.forum.service.article.repository.entity.ArticleTagsDO;
import com.github.paicoding.forum.service.article.service.*;
import com.github.paicoding.forum.service.comment.repository.entity.CommentDO;
import com.github.paicoding.forum.service.comment.service.CommentReadService;
import com.github.paicoding.forum.service.sidebar.service.SidebarService;
import com.github.paicoding.forum.service.user.repository.entity.UserFootDO;
import com.github.paicoding.forum.service.user.repository.entity.UserInfoDO;
import com.github.paicoding.forum.service.user.repository.mapper.UserInfoMapper;
import com.github.paicoding.forum.service.user.service.UserFootService;
import com.github.paicoding.forum.service.user.service.UserService;
import com.github.paicoding.forum.web.controller.article.vo.ArticleDetailVo;
import com.github.paicoding.forum.web.controller.article.vo.ArticleEditVo;
import com.github.paicoding.forum.web.controller.article.vo.ArticleStatusVo;
import com.github.paicoding.forum.web.controller.home.helper.IndexRecommendHelper;
import com.github.paicoding.forum.web.global.vo.ResultVo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * 返回json格式数据
 *
 * @author XuYifei
 * @date 2024/7/6
 */
@Slf4j
@RequestMapping(path = "article/api")
@RestController
public class ArticleRestController {
    @Autowired
    private ArticleReadService articleReadService;
    @Autowired
    private UserFootService userFootService;
    @Autowired
//    private TagService tagService;
    private ArticleReadService articleService;
    @Autowired
    private ArticleWriteService articleWriteService;

    @Autowired
    private ArticleRecommendService articleRecommendService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private CommentReadService commentService;

    @Autowired
    private SidebarService sidebarService;

    @Autowired
    IndexRecommendHelper indexRecommendHelper;

    @Autowired
    ArticleContentService articleContentService;
    @Autowired
    private ArticleTagsDao articleTagsDao;

    /**
     * 获取热门文章
     * @param topArticles
     * @return
     */
    @GetMapping("/top")
    public ResVo<List<ArticleItemDo>> getTopArticleItems(
            @RequestParam(name = "top", required = false, defaultValue = "30") Integer topArticles) {
        List<ArticleItemDo> topItems = articleReadService.getTopArticleItems(topArticles);
        return ResVo.ok(topItems);
    }

    /**
     * 发布文章
     *
     * @return
     */
    @Permission(role = UserRole.ALL)
    @PostMapping(path = "/save")
    public ResVo<Long> save(@RequestBody ArticlePostReq req, HttpServletResponse response) throws IOException {
        Long id = articleWriteService.saveArticle(req, ReqInfoContext.getReqInfo().getUserId());
        return ResVo.ok(id);
    }

    /**
     * 根据文章ID获取指定文章详情
     * @param articleId
     * @return
     */
    @GetMapping("/get")
    public ResVo<ArticleDetailVo> getArticleDetail(Long articleId) {
        ArticleDO article = articleReadService.getArticleById(articleId);
        if (article == null) throw new RuntimeException("查看文章不存在");
        // 文章计数+1
        articleWriteService.addArticleViews(articleId);
        String content = articleContentService.getArticleContentById(articleId).getContent();
        List<CommentDTO> comments = commentService.getArticleComments(articleId);
        UserInfoDO author = userInfoMapper.selectById(article.getUserId());
        UserFootDO foot = userFootService.queryUserFoot(articleId, 1, ReqInfoContext.getReqInfo().getUserId());
        ArticleDetailVo vo = new ArticleDetailVo();
        vo.setArticleId(articleId);
        vo.setTitle(article.getTitle());
        vo.setAuthorId(author.getId());
        vo.setAuthorName(author.getUserName());
        vo.setPublishTime(article.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        vo.setContent(content);
        vo.setLikes(article.getLikes());
        vo.setCommentCount(article.getComments());
        vo.setAuthorAvatar(author.getPhoto());
        vo.setComments(comments);
        return ResVo.ok(vo);
    }

    /**
     * 获取某个Tag下的文章列表
     * @param tag
     * @return
     */
    @GetMapping("/category")
    public ResVo<List<ArticleItemDo>> getArticlesByTag(String tag) {
        Long tagCode = ArticleTagEnum.fromDescription(tag);
        if (tag == null || tag.isEmpty() || tagCode == null) throw new RuntimeException("该标签不存在!");
        List<ArticleItemDo> data = articleReadService.queryArticlesByTag(tagCode);
        return ResVo.ok(data);
    }

    @GetMapping("/status")
    public ResVo<ArticleStatusVo> getArticleStatus(Long articleId) {
        ArticleDO article = articleReadService.getArticleById(articleId);
        UserFootDO foot = userFootService.
                queryUserFoot(articleId, 1, ReqInfoContext.getReqInfo().getUserId());
        boolean isLike = foot != null && foot.getPraiseStat() == 1;
        ArticleStatusVo vo = new ArticleStatusVo();
        vo.setIsLike(isLike);
        vo.setLikeCount(article.getLikes());
        vo.setCommentCount(article.getComments());
        return ResVo.ok(vo);
    }

//    /**
//     * 文章删除
//     *
//     * @param articleId
//     * @return
//     */
//    @Permission(role = UserRole.LOGIN)
//    @RequestMapping(path = "delete")
//    public ResVo<Boolean> delete(@RequestParam(value = "articleId") Long articleId) {
//        articleWriteService.deleteArticle(articleId, ReqInfoContext.getReqInfo().getUserId());
//        return ResVo.ok(true);
//    }
}
