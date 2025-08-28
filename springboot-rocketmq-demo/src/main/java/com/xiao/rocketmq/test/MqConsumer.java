package com.xiao.rocketmq.test;

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

        // 创建一个回调函数
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            // 处理业务逻辑，如果处理失败，需要返回 ConsumeConcurrentlyStatus.RECONSUME_LATER
            for (MessageExt msg : msgs) {
                System.out.println(msg);
                System.out.println("收到的消息内容：" + new String(msg.getBody()));
            }
            // 返回消费成功的对象
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        // 启动消费者
        consumer.start();
        System.out.println("消费者已经启动");
    }
}
