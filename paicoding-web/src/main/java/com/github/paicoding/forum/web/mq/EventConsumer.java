package com.github.paicoding.forum.web.mq;

import com.github.paicoding.forum.api.model.enums.DocumentTypeEnum;
import com.github.paicoding.forum.api.model.enums.NotifyTypeEnum;
import com.github.paicoding.forum.api.model.enums.OperateTypeEnum;
import com.github.paicoding.forum.api.model.vo.user.UserRelationReq;
import com.github.paicoding.forum.service.article.repository.entity.ArticleDO;
import com.github.paicoding.forum.service.article.service.ArticleReadService;
import com.github.paicoding.forum.service.article.service.ArticleWriteService;
import com.github.paicoding.forum.service.notify.service.NotifyService;
import com.github.paicoding.forum.service.user.repository.entity.UserFootDO;
import com.github.paicoding.forum.service.user.repository.entity.UserRelationDO;
import com.github.paicoding.forum.service.user.service.UserFootService;
import com.github.paicoding.forum.service.user.service.UserRelationService;
import com.github.paicoding.forum.web.mq.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EventConsumer {

    private ArticleWriteService articleWriteService;
    private UserFootService userFootService;
    private ArticleReadService articleReadService;
    private NotifyService notifyService;
    private UserRelationService userRelationService;

    @RabbitListener(queues = RabbitConfig.LIKE_QUEUE)
    public void handleLikeEvent(Map<String, Object> event) {
        Long userId = (Long) event.get("userId");
        Long articleId = (Long) event.get("articleId");
        Long authorId = (Long) event.get("authorId");
        Integer type = (Integer) event.get("likeType");
        // todo 这里需要考虑并发问题
        // 点赞事件
        if (type == 0) {
            userFootService.saveOrUpdateUserFoot
                    (DocumentTypeEnum.ARTICLE, articleId, authorId, userId, OperateTypeEnum.PRAISE);
            ArticleDO article = articleReadService.getArticleById(articleId);
            article.setLikes(article.getLikes() + 1);
            articleWriteService.updateArticle(article);
            UserFootDO footDO = new UserFootDO();
            footDO.setUserId(userId);
            footDO.setArticleId(articleId);
            footDO.setArticleAuthorId(authorId);
            notifyService.saveArticleNotify(footDO, NotifyTypeEnum.PRAISE);
        } else if (type == 1) {
            // 取消点赞事件
            userFootService.saveOrUpdateUserFoot
                    (DocumentTypeEnum.ARTICLE, articleId, authorId, userId, OperateTypeEnum.CANCEL_PRAISE);
            ArticleDO article = articleReadService.getArticleById(articleId);
            article.setLikes(article.getLikes() - 1);
            articleWriteService.updateArticle(article);
        }
    }

    @RabbitListener(queues = RabbitConfig.FOLLOW_QUEUE)
    public void handleFollowEvent(Map<String, Object> event) {
        Long userId = (Long) event.get("userId");
        Long followedId = (Long) event.get("followedId");
        Integer type = (Integer) event.get("followType");
        if (type == 0) {
            //关注作者
            UserRelationReq req = new UserRelationReq();
            req.setUserId(userId);
            req.setFollowUserId(followedId);
            req.setFollowed(true);
            userRelationService.saveUserRelation(req);
            UserRelationDO relation = new UserRelationDO();
            relation.setUserId(userId);
            relation.setFollowUserId(followedId);
            relation.setFollowState(1);
            notifyService.saveUserNotify(relation, NotifyTypeEnum.FOLLOW);
        } else if (type == 1) {
            // 取关作者
            UserRelationReq req = new UserRelationReq();
            req.setUserId(userId);
            req.setFollowUserId(followedId);
            req.setFollowed(false);
            userRelationService.saveUserRelation(req);
            UserRelationDO relation = new UserRelationDO();
            relation.setUserId(userId);
            relation.setFollowUserId(followedId);
            relation.setFollowState(2);
            notifyService.saveUserNotify(relation, NotifyTypeEnum.CANCEL_FOLLOW);
        }
    }

    @RabbitListener(queues = RabbitConfig.COMMENT_QUEUE)
    public void handleCommentEvent(Map<String, Object> event) {
        Long userId = (Long) event.get("userId");
        Long articleId = (Long) event.get("articleId");
        Long parentCommentId = (Long) event.get("parentCommentId");
        Integer commentType = (Integer) event.get("commentType");
        ArticleDO article = articleReadService.getArticleById(articleId);
        article.setComments(article.getComments() + 1);
        articleWriteService.updateArticle(article);
        if (commentType == 0) {
            // 评论文章
            userFootService.saveOrUpdateUserFoot
                    (DocumentTypeEnum.ARTICLE,articleId,article.getUserId(),userId,OperateTypeEnum.COMMENT);
        }else if (commentType == 1) {
            // 对评论的评论
            userFootService.saveOrUpdateUserFoot(
                    DocumentTypeEnum.COMMENT,articleId,article.getUserId(),userId,OperateTypeEnum.COMMENT);
        }
    }

    @Autowired
    public void setArticleWriteService(ArticleWriteService articleWriteService) {
        this.articleWriteService = articleWriteService;
    }

    @Autowired
    public void setUserFootService(UserFootService userFootService) {
        this.userFootService = userFootService;
    }

    @Autowired
    public void setArticleReadService(ArticleReadService articleReadService) {
        this.articleReadService = articleReadService;
    }

    @Autowired
    public void setNotifyService(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    @Autowired
    public void setUserRelationService(UserRelationService userRelationService) {
        this.userRelationService = userRelationService;
    }
}
