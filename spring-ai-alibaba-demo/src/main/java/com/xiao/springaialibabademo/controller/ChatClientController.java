package com.xiao.springaialibabademo.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.xiao.springaialibabademo.tools.TimeTool;
import com.xiao.springaialibabademo.tools.WeatherTool;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("client")
public class ChatClientController {

    private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";

    private final ChatClient chatClient;

    public ChatClientController(ChatModel chatModel) {
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


    /**
     * ChatClient 简单调用
     */
    @GetMapping("/simple/chat")
    public String simpleChat(@RequestParam(value = "query", defaultValue = "简单介绍一下自己")String query,
                             @RequestParam(value = "chatId", defaultValue = "1") String chatId) {

        return chatClient.prompt()
                // 添加工具，工具的调用由 chatClient 内部的智能决策系统根据用户查询内容来决定
                .tools(new TimeTool(), new WeatherTool())
                .user(query)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                // 纯文本响应
                .content();
                // 结构化响应
//                .entity(Object.class);
    }


    /**
     * ChatClient 流式调用
     */
    @GetMapping("/stream/chat")
    public Flux<String> streamChat(@RequestParam(value = "query", defaultValue = "简单介绍一下自己")String query,
                                   @RequestParam(value = "chatId", defaultValue = "2") String chatId,
                                   HttpServletResponse response) {
        // 避免返回乱码
        response.setCharacterEncoding("UTF-8");
        return chatClient.prompt().user(query).advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId)).stream().content();
    }

}
