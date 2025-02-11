package com.github.paicoding.forum.web.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    public static final String LIKE_QUEUE = "like.queue";
    public static final String FOLLOW_QUEUE = "follow.queue";
    public static final String COMMENT_QUEUE = "comment.queue";
    public static final String EXCHANGE = "event.exchange";

    @Bean
    public Queue likeQueue() {
        return new Queue(LIKE_QUEUE);
    }

    @Bean
    public Queue followQueue() {
        return new Queue(FOLLOW_QUEUE);
    }

    @Bean
    public Queue commentQueue() {
        return new Queue(COMMENT_QUEUE);
    }

    @Bean
    public Exchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding likeBinding(Queue likeQueue, Exchange eventExchange) {
        return BindingBuilder.bind(likeQueue).to(eventExchange).with("like.event").noargs();
    }

    @Bean
    public Binding followBinding(Queue followQueue, Exchange eventExchange) {
        return BindingBuilder.bind(followQueue).to(eventExchange).with("follow.event").noargs();
    }

    @Bean
    public Binding commentBinding(Queue commentQueue, Exchange eventExchange) {
        return BindingBuilder.bind(commentQueue).to(eventExchange).with("comment.event").noargs();
    }
}
