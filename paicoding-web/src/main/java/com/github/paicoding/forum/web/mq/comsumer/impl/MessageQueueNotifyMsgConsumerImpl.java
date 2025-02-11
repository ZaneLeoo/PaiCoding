package com.github.paicoding.forum.web.mq.comsumer.impl;

import com.github.paicoding.forum.api.model.event.MessageQueueEvent;
import com.github.paicoding.forum.service.comment.repository.entity.CommentDO;
import com.github.paicoding.forum.web.mq.comsumer.MessageQueueNotifyMsgConsumer;

public class MessageQueueNotifyMsgConsumerImpl implements MessageQueueNotifyMsgConsumer {

    @Override
    public void saveCommentNotify(MessageQueueEvent<CommentDO> event) {

    }
}
