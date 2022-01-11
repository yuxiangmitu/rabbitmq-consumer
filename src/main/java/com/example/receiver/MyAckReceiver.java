package com.example.receiver;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 手动确认消息接收处理类
 * @Author cxl
 * @Date 2022-01-10
 */
@Component
public class MyAckReceiver implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 传递消息的时候用的 map 传递, 将 map 从 message 内取出
            String msg = message.toString();
            // 可以点进 Message 里面看源码, 单引号之间的数据就是 map 消息数据
            String[] msgArray = msg.split("'");
            Map<String, String> msgMap = mapStringToMap(msgArray[1].trim(),3);
            String messageId = msgMap.get("messageId");
            String messageData = msgMap.get("messageData");
            String createTime = msgMap.get("createTime");

            if ("testDirectQueue".equals(message.getMessageProperties().getConsumerQueue())){
                System.out.println("消费的消息来自的队列名为：" + message.getMessageProperties().getConsumerQueue());
                System.out.println("消息成功消费到 messageId：" + messageId + " messageData:" + messageData + " createTime:" + createTime);
                System.out.println("执行TestDirectQueue中的消息的业务处理流程......");

            }

            if ("fanout.A".equals(message.getMessageProperties().getConsumerQueue())){
                System.out.println("消费的消息来自的队列名为：" + message.getMessageProperties().getConsumerQueue());
                System.out.println("消息成功消费到 messageId：" + messageId+" messageData:" + messageData + " createTime:" + createTime);
                System.out.println("执行fanout.A中的消息的业务处理流程......");
            }

            // basicAck用于确认消息，basicNack和basicReject都用于拒绝消息，但basicReject一次只能拒绝一条消息
            // 第二个参数为 true 时，手动确认可以被批处理，可以一次性确认 delivery_tag 小于等于传入值的所有消息
            channel.basicAck(deliveryTag, true);
            // 第二个参数为true时会把消息重新放回队列，需要根据业务逻辑判断什么时候拒绝
//			channel.basicReject(deliveryTag, false);
        } catch (Exception e) {
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }
    }

    /**
     * {key=value, key=value, key=value} 格式转换成 map
     */
    private Map<String, String> mapStringToMap(String str, int entryNum) {
        String[] strs = str.split(",",entryNum);
        Map<String, String> map = new HashMap<String, String>(16);
        for (String string : strs) {
            String key = string.split("=")[0].trim();
            String value = string.split("=")[1];
            map.put(key, value);
        }
        return map;
    }

}
