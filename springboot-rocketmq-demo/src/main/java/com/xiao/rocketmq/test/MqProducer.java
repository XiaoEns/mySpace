package com.xiao.rocketmq.test;

import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 模拟消息生产者
 */
public class MqProducer {

    private final static String NAMESERVER_ADDR = "localhost:9876";

    private final static String GROUP_NAME = "my_transaction_consumer";

    private final static String TOPIC_NAME = "TransactionTopic";


    public static void main(String[] args) throws Exception {
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

        // 发送 10 条半消息，消费者是收不到半消息的
        String[] tags = {"tagA", "tagB", "tagC", "tagD","tagE"};
        for (int i = 0; i < 10; i++) {
            // 这里发送的消息首先会保存到 RMQ_SYS_TRANS_HALF_TOPIC 主题中
            Message message = new Message(TOPIC_NAME, tags[i % tags.length], "key" + i, ("测试事务消息" + tags[i % tags.length]+"_"+i).getBytes(StandardCharsets.UTF_8));
            TransactionSendResult transactionSendResult = producer.sendMessageInTransaction(message, null);
            System.out.println("本次发送的消息是=" + new String(message.getBody()));
            System.out.printf("%s%n", transactionSendResult);
            Thread.sleep(10);
        }
        System.out.println("==========所有消息发送完成======");
    }
}
