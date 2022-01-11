package com.example.config;

import com.example.receiver.MyAckReceiver;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 消息监听配置类
 * @Author cxl
 * @Date 2022-01-10
 */
//@Configuration
public class MessageListenerConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;

    /**
     * 手动确认消息接收处理类
     */
    @Autowired
    private MyAckReceiver myAckReceiver;

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        // RabbitMQ默认是自动确认，这里改为手动确认消息
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        // 设置一个队列
//        container.setQueueNames("testDirectQueue");
        // 同时设置多个队列, 前提是队列都必须存在
         container.setQueueNames("testDirectQueue","fanout.A");

        // 另一种设置队列的方法, 这种情况下要设置多个, 使用addQueues
        // container.setQueues(new Queue("testDirectQueue",true));
        // container.addQueues(new Queue("testDirectQueue2",true));
        // container.addQueues(new Queue("testDirectQueue3",true));

        container.setMessageListener(myAckReceiver);
        return container;
    }

}
