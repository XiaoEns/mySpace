package com.xiao.springaialibabademo.controller;


import com.alibaba.cloud.ai.graph.CompileConfig;
import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.action.EdgeAction;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/graph/stream")
public class WorkflowController {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowController.class);

    private CompiledGraph compiledGraph;

    public WorkflowController(@Qualifier("workflowGraph") StateGraph stateGraph,
                                     ObjectProvider<ObservationRegistry> observationRegistry) throws GraphStateException {
//        this.compiledGraph = stateGraph.compile(CompileConfig.builder()
//                .withLifecycleListener(new GraphObservationLifecycleListener(
//                        observationRegistry.getIfUnique(() -> ObservationRegistry.NOOP)))
//                .build());

        this.compiledGraph = stateGraph.compile(CompileConfig.builder().build());
    }

    /**
     * 输入商品评价
     * @param query
     * @return
     * @throws Exception
     */
    @GetMapping("/chat")
    public String simpleChat(String query) throws Exception {

        return compiledGraph.invoke(Map.of("input", query)).get().value("solution").get().toString();
    }


    // 定义节点
    public static class QuestionTypeNode implements NodeAction {
        private static final String CLASSIFIER_PROMPT_TEMPLATE = """
                ### 职位描述
                你是一个文本分类引擎，能够分析文本数据并根据用户输入或自动确定的类别进行分类。
                ### 任务
                你的任务是仅为输入文本分配一个类别，且输出中只能返回一个类别。此外，你需要从文本中提取与分类相关的关键词。
                ### 格式
                输入文本为：{inputText}。类别以类别列表的形式指定：{categories}。为了提高分类准确性，可能会包含分类说明：{classificationInstructions}。
                ### 约束条件
                请确保您的回复中仅包含JSON数组，不要包含任何其他内容。
            """;

        private SystemPromptTemplate systemPromptTemplate;

        private ChatClient chatClient;

        private String inputText;

        private List<String> categories;

        private List<String> classificationInstructions;

        private String inputTextKey;

        private String outputKey;

        public QuestionTypeNode(ChatClient chatClient, String inputTextKey, List<String> categories,
                                      List<String> classificationInstructions, String outputKey) {
            this.chatClient = chatClient;
            this.inputTextKey = inputTextKey;
            this.categories = categories;
            this.classificationInstructions = classificationInstructions;
            this.systemPromptTemplate = new SystemPromptTemplate(CLASSIFIER_PROMPT_TEMPLATE);
            this.outputKey = outputKey;
        }

        @Override
        public Map<String, Object> apply(OverAllState state) throws Exception {
            if (StringUtils.hasLength(inputTextKey)) {
                this.inputText = (String) state.value(inputTextKey).orElse(this.inputText);
            }

            ChatResponse response = chatClient.prompt()
                    .system(systemPromptTemplate.render(Map.of(
                            "inputText", inputText,
                            "categories", categories,
                            "classificationInstructions", classificationInstructions)))
                    .user(inputText)
//                    .messages(messages) // 历史消息，作为示例，用于大模型学习展示格式
                    .call()
                    .chatResponse();

            Map<String, Object> updatedState = new HashMap<>();
            updatedState.put(outputKey, response.getResult().getOutput().getText());
            if (state.value("messages").isPresent()) {
                updatedState.put("messages", response.getResult().getOutput());
            }

            return updatedState;
        }

        public static QuestionTypeNode.Builder builder() {
            return new QuestionTypeNode.Builder();
        }

        public static class Builder {

            private String inputTextKey;

            private ChatClient chatClient;

            private List<String> categories;

            private List<String> classificationInstructions;

            private String outputKey;

            public Builder inputTextKey(String input) {
                this.inputTextKey = input;
                return this;
            }

            public Builder chatClient(ChatClient chatClient) {
                this.chatClient = chatClient;
                return this;
            }

            public Builder categories(List<String> categories) {
                this.categories = categories;
                return this;
            }

            public Builder classificationInstructions(List<String> classificationInstructions) {
                this.classificationInstructions = classificationInstructions;
                return this;
            }

            public Builder outputKey(String outputKey) {
                this.outputKey = outputKey;
                return this;
            }

            public QuestionTypeNode build() {
                return new QuestionTypeNode(chatClient, inputTextKey, categories, classificationInstructions, outputKey);
            }

        }
    }

    public static class RecordingNode implements NodeAction {

        @Override
        public Map<String, Object> apply(OverAllState state) throws Exception {

            String feedback = (String) state.value("classifier_output").get();

            Map<String, Object> updatedState = new HashMap<>();
            if (feedback.contains("正反馈")) {
                logger.info("Received positive feedback: {}", feedback);
                updatedState.put("solution", "用户的反馈是正反馈");
            }
            else {
                logger.info("Received negative feedback: {}", feedback);
                updatedState.put("solution", feedback);
            }

            return updatedState;
        }

    }


    // 定义边的调度逻辑
    public static class FeedbackQuestionDispatcher implements EdgeAction {

        @Override
        public String apply(OverAllState state) throws Exception {

            String classifierOutput = (String) state.value("classifier_output").orElse("");
            logger.info("classifierOutput: {}", classifierOutput);

            if (classifierOutput.contains("正反馈")) {
                return "正反馈";
            }
            return "负反馈";
        }

    }


    // 定义边的调度逻辑
    public static class SpecificQuestionDispatcher implements EdgeAction {

        @Override
        public String apply(OverAllState state) throws Exception {

            String classifierOutput = (String) state.value("classifier_output").orElse("");
            logger.info("classifierOutput: {}", classifierOutput);

            Map<String, String> classifierMap = new HashMap<>();
            classifierMap.put("售后服务", "售后服务");
            classifierMap.put("产品质量", "产品质量");
            classifierMap.put("运输", "运输");

            for (Map.Entry<String, String> entry : classifierMap.entrySet()) {
                if (classifierOutput.contains(entry.getKey())) {
                    return entry.getValue();
                }
            }

            return "其他";
        }

    }

}
