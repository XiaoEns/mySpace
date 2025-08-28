package com.xiao.rocketmq.demo;

import com.alibaba.fastjson2.JSON;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 模拟消息消费者
 */
public class MqConsumer {

    private final static String NAMESERVER_ADDR = "localhost:9876";

    private final static String GROUP_NAME = "my_transaction_consumer";

    private final static String TOPIC_NAME = "TransactionTopic";


    public static void main(String[] args) throws Exception {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(GROUP_NAME);
        consumer.setNamesrvAddr(NAMESERVER_ADDR);
        consumer.subscribe(TOPIC_NAME, "*");

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                // 接收到消息，进行业务处理
                System.out.println(msg);
                Order order = JSON.parseObject(new String(msg.getBody()), Order.class);
                System.out.println("收到的消息内容：" + order);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        // 启动消费者
        consumer.start();
        System.out.println("消费者已经启动");
    }
}
