package com.xiao.springaialibabademo.controller;


import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("agent")
public class AgentsController {


    private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";

    private final ChatClient chatClient;

    public AgentsController(ChatModel chatModel) {
        // 会话聊天记忆
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                // 这里使用内存来存储会话上下文，如果需要使用mysql，redis等，需要自定义 ChatMemoryRepository 实现类
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();

        // 构造ChatClient，设置相关参数
        this.chatClient = ChatClient.builder(chatModel)
                // 设置默认的 prompt
                .defaultSystem(DEFAULT_PROMPT)
                // advisor 增强器
                .defaultAdvisors(
                        // 添加对话记忆
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        // 添加日志增强器
                        new SimpleLoggerAdvisor()
                )
                // 设置 ChatClient 中 ChatModel 的 Options 参数
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                // 设置 tok-k 采样参数
                                .withTopK(50)
                                // 设置 tok-p 采样参数
                                .withTopP(0.7)
                                // 设置输出随机性, 数值越大输出越随机
                                .withTemperature(0.8)
                                // 设置最大输出长度
                                .withMaxToken(250)
                                .build()
                )
                .build();
    }


}
