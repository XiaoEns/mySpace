package com.xiao.rocketmq.demo;

import com.alibaba.fastjson2.JSON;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionListenerImpl implements TransactionListener {

    /**
     * 执行本地事务
     * 当事务 half 消息发送成功，这个方法将被执行
     * 事务的 half 消息是发到 RMQ_SYS_TRANS_OP_HALF_TOPIC 的 topic 中
     *
     * @param msg 消息
     * @param arg arg 自定义业务参数
     * @return {@link LocalTransactionState}
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        System.out.println("===执行本地事务===" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        LocalTransactionState state = LocalTransactionState.UNKNOW;
        try {
            Order order = JSON.parseObject(new String(msg.getBody()), Order.class);
            System.out.println(order);
            // orderMapper.insert(order); 保存订单数据

//            throw new Exception("模拟异常");

            System.out.println("====本地事务执行成功===");
            state = LocalTransactionState.COMMIT_MESSAGE;
        } catch (Exception e) {
            System.out.println("====本地事务执行失败===");
            state = LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return state;
    }


    /**
     * 检查本地事务
     * 回查本地事务状态，当 half 消息没响应时调用
     *
     * @param msg 消息
     * @return {@link LocalTransactionState}
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        System.out.println("===开始回查本地事务状态===" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        LocalTransactionState state = LocalTransactionState.UNKNOW;

        Order order = JSON.parseObject(new String(msg.getBody()), Order.class);
        if (existOrder(order.getId())) {
            state = LocalTransactionState.COMMIT_MESSAGE;
        }
        return state;
    }

    private static boolean existOrder(Long orderId) {
        // 根据 id 查询，如果存在 order，则说明事务执行成功
//        Order existOrder = orderMapper.selectById(order.getId());
//        if (existOrder == null) return false;
        return true;
    }

}
