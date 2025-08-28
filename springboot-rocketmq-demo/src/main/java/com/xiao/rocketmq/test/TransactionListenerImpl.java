package com.xiao.rocketmq.test;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.time.LocalDateTime;

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
        String tags = msg.getTags();
        System.out.println("============执行 executeLocalTransaction 方法，;消息内容是="+new String(msg.getBody()));
        if (StringUtils.contains(tags, "tagA")) {  // tagA 消息会提交
            return LocalTransactionState.COMMIT_MESSAGE;
        } else if (StringUtils.contains(tags, "tagB")) { // tagB 消息会回滚
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.UNKNOW;
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
        String tags = msg.getTags();
        System.out.println("============执行 checkLocalTransaction 方法，;消息内容是="+new String(msg.getBody()) + ";time" + LocalDateTime.now());
        if (StringUtils.contains(tags, "tagC")) {  // tagC 消息会提交
            return LocalTransactionState.COMMIT_MESSAGE;
        } else if (StringUtils.contains(tags, "tagD")) {  // tagD 消息会回滚
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.UNKNOW;  // 默认回查 15 次
    }

}
