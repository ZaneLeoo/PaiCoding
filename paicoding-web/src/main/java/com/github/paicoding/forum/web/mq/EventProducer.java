package com.github.paicoding.forum.web.mq;

import com.github.paicoding.forum.web.mq.config.RabbitConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendLikeEvent(Long userId, Long articleId, Long authorId, Integer likeType) {
        Map<String, Object> event = new HashMap<>();
        event.put("userId", userId);
        event.put("articleId", articleId);
        event.put("authorId", authorId);
        event.put("likeType", likeType);
        amqpTemplate.convertAndSend(RabbitConfig.EXCHANGE, "like.event", event);
    }

    public void sendFollowEvent(Long userId, Long followedId, Integer followType) {
        Map<String, Object> event = new HashMap<>();
        event.put("userId", userId);
        event.put("followedId", followedId);
        event.put("followType", followType);
        amqpTemplate.convertAndSend(RabbitConfig.EXCHANGE, "follow.event", event);
    }

    public void sendCommentEvent(Long userId, Long articleId, Long parentCommentId, Integer commentType) {
        Map<String, Object> event = new HashMap<>();
        event.put("userId", userId);
        event.put("articleId", articleId);
        event.put("parentCommentId", parentCommentId);
        event.put("commentType", commentType);
        amqpTemplate.convertAndSend(RabbitConfig.EXCHANGE, "comment.event", event);
    }


}
