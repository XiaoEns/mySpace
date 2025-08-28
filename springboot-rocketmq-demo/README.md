springboot 整合 rocketmq

## 事务消息

### 基础概念
RocketMQ的事务消息可以用于实现基于可靠消息的最终一致性的分布式事务。
可靠消息最终一致性事务适合执行周期长且实时性要求不高的场景。
引入消息机制后，同步的事务操作变为基于消息执行的异步操作, 避免了分布式事务中的同步阻塞操作的影响，并实现了两个服务的解耦。

### 发送流程
Producer向broker发送半消息(Half Message)
其实半消息本质上就是往 RMQ_SYS_TRANS_HALF_TOPIC 这个Topic发送了一条消息，保存在特殊队列中

Producer得到发送结果响应 
+ SEND_OK 
+ FLUSH_DISK_TIMEOUT 
+ FLUSH_SLAVE_TIMEOUT 
+ SLAVE_NOT_AVAILABLE
根据发送结果执行本地事务，如果失败的话半消息对Consumer不可见，本地事务不执行

本地事务执行完成后返回状态 
+ Commit：本地事务执行成功，消息对Consumer可见，Consumer可以消费消息，但不保证Consumer一定消费成功，但这一块已经不属于事务消息的范畴了，不做额外处理的话，利用MQ的ack机制，保证最终一致性，注意Consumer要保证幂等性 
+ Rollback：本地事务回滚，消息对Consumer不可见，Consumer无法消费 
+ Unknown：如果本地事务返回Unknown状态，或者MQ迟迟收不到Producer的返回结果，那MQ会发起回查回调Producer的检查本地事务方法，重新返回状态，这个属于补偿机制，默认回查15次