package com.xiao.rocketmq.demo;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 模拟消息生产者: 模拟订单创建
 */
public class MqProducer {

    private final static String NAMESERVER_ADDR = "localhost:9876";

    private final static String GROUP_NAME = "my_transaction_consumer";

    private final static String TOPIC_NAME = "TransactionTopic";


    public static void main(String[] args) throws Exception{
        // 定义事务监听器
        TransactionListener transactionListener = new TransactionListenerImpl();
        // 定义生产者
        TransactionMQProducer producer = new TransactionMQProducer(GROUP_NAME);
        producer.setNamesrvAddr(NAMESERVER_ADDR);
        // 定义线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100), (runnable, executor) -> {
            BlockingQueue<Runnable> queue = executor.getQueue();
            try {
                queue.put(runnable);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        // 设置线程池
        producer.setExecutorService(threadPoolExecutor);
        // 设置事务监听器
        producer.setTransactionListener(transactionListener);
        // 启动生产者
        producer.start();
        System.out.println("生产者已经启动");

        // 事务消息发送
        Order order = new Order();
        order.setId(1L);
        order.setName("order1");
        Message message = new Message(TOPIC_NAME, JSON.toJSONString(order).getBytes());
        TransactionSendResult result = producer.sendMessageInTransaction(message, null);
        System.out.println("send result: " + result);
    }
}
