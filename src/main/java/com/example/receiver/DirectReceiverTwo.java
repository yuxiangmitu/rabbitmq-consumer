package com.example.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description 直连型交换机 消息接收监听类
 * @Author cxl
 * @Date 2022-01-10
 */
@Component
@RabbitListener(queues = "testDirectQueue")
public class DirectReceiverTwo {

    @RabbitHandler
    public void process(Map testMessage) {
        System.out.println("DirectReceiver第二个消费者收到消息 : " + testMessage.toString());
    }

}
