package com.github.paicoding.forum.web.controller.comment.rest;

import com.github.paicoding.forum.api.model.context.ReqInfoContext;
import com.github.paicoding.forum.api.model.vo.ResVo;
import com.github.paicoding.forum.api.model.vo.comment.dto.CommentDTO;
import com.github.paicoding.forum.api.model.vo.constants.StatusEnum;
import com.github.paicoding.forum.core.util.NumUtil;
import com.github.paicoding.forum.service.article.repository.entity.ArticleDO;
import com.github.paicoding.forum.service.article.service.ArticleReadService;
import com.github.paicoding.forum.service.comment.repository.entity.CommentDO;
import com.github.paicoding.forum.service.comment.service.CommentReadService;
import com.github.paicoding.forum.service.comment.service.CommentWriteService;
import com.github.paicoding.forum.web.mq.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论
 *
 * @author XuYifei
 * @date : 2024-07-12
 **/
@RestController
@RequestMapping(path = "comment/api")
public class CommentRestController {
    @Autowired
    private CommentReadService commentReadService;
    @Autowired
    private CommentWriteService commentWriteService;
    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private EventProducer eventProducer;

    /**
     * 评论列表页
     *
     * @param articleId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "list")
    public ResVo<List<CommentDTO>> list(Long articleId) {
        if (NumUtil.nullOrZero(articleId)) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章id为空");
        }
        List<CommentDTO> comments = commentReadService.
                getArticleComments(articleId);
        return ResVo.ok(comments);
    }

    /**
     * 保存评论
     *
     * @return
     */
//    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "add")
    @ResponseBody
    public ResVo<String> add(@RequestBody CommentDO comment) {
        ArticleDO article = articleReadService.getArticleById(comment.getArticleId());
        if (article == null) return ResVo.fail(StatusEnum.ARTICLE_NOT_EXISTS, "文章id为空");
        if (article.getStatus() == 0) return ResVo.fail(StatusEnum.ARTICLE_NOT_PUBLISH, "文章未发布");
        // 保存评论
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        comment.setUserId(userId);
        comment.setLikes(0);
        comment.setDeleted(0);
        commentWriteService.addComment(comment);
        // 发布事件
        eventProducer.sendCommentEvent
                (userId,article.getId(),comment.getParentCommentId(),(comment.getParentCommentId() == null ? 1 : 0));
        return ResVo.ok("ok");
    }

    /**
     * 删除评论
     *
     * @param commentId
     * @return
     */
//    @Permission(role = UserRole.LOGIN)
    @RequestMapping(path = "delete")
    public ResVo<Boolean> delete(Long commentId) {
        commentWriteService.deleteComment(commentId);
        return ResVo.ok(true);
    }
}
