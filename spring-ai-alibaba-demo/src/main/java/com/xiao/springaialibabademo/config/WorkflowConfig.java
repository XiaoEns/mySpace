package com.xiao.springaialibabademo.config;

import com.alibaba.cloud.ai.graph.GraphRepresentation;
import com.alibaba.cloud.ai.graph.KeyStrategy;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import com.xiao.springaialibabademo.controller.WorkflowController;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.cloud.ai.graph.StateGraph.END;
import static com.alibaba.cloud.ai.graph.StateGraph.START;
import static com.alibaba.cloud.ai.graph.action.AsyncEdgeAction.edge_async;
import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;

@Configuration
public class WorkflowConfig {

    @Bean
    public StateGraph workflowGraph(ChatModel chatModel) throws GraphStateException {

        ChatClient chatClient = ChatClient.builder(chatModel).defaultAdvisors(new SimpleLoggerAdvisor()).build();

        // 第一个节点，判断用户输入的评价是属于 正反馈/负反馈
        WorkflowController.QuestionTypeNode feedbackClassifier = WorkflowController.QuestionTypeNode.builder()
                .chatClient(chatClient)
                .inputTextKey("input")
                .outputKey("classifier_output")
                .categories(List.of("正反馈", "负反馈"))
                .classificationInstructions(
                        List.of("用户会给出评价，尝试理解用户的感受，判断是属于正反馈还是负反馈"))
                .build();


        // 第二个节点，判断用户输入的具体问题属于 售后服务/运输/产品质量/其他
        WorkflowController.QuestionTypeNode specificQuestionClassifier = WorkflowController.QuestionTypeNode.builder()
                .chatClient(chatClient)
                .inputTextKey("input")
                .outputKey("classifier_output")
                .categories(List.of("售后服务", "运输", "产品质量", "其他"))
                .classificationInstructions(List
                        .of("客户想从我们这里获得什么样的服务或帮助？根据你的理解，对这个问题进行分类，问题分类有售后服务/运输/产品质量/其他"))
                .build();

        StateGraph stateGraph = new StateGraph("Consumer Service Workflow Demo", () -> {
            Map<String, KeyStrategy> strategies = new HashMap<>();
            strategies.put("input", new ReplaceStrategy());
            strategies.put("classifier_output", new ReplaceStrategy());
            strategies.put("solution", new ReplaceStrategy());
            return strategies;
        });


        // 添加节点
        stateGraph.addNode("feedback_classifier", node_async(feedbackClassifier))
                .addNode("specific_question_classifier", node_async(specificQuestionClassifier))
                .addNode("recorder", node_async(new WorkflowController.RecordingNode()));

        // 添加边
        stateGraph.addEdge(START, "feedback_classifier")
                .addConditionalEdges("feedback_classifier",
                        edge_async(new WorkflowController.FeedbackQuestionDispatcher()),
                        Map.of("正反馈", "recorder", "负反馈", "specific_question_classifier"));

        stateGraph.addConditionalEdges("specific_question_classifier",
                        edge_async(new WorkflowController.SpecificQuestionDispatcher()),
                        Map.of("售后服务", "recorder",
                                "运输", "recorder",
                                "产品质量", "recorder",
                                "其他", "recorder"));

        stateGraph.addEdge("recorder", END);

        GraphRepresentation graphRepresentation = stateGraph.getGraph(GraphRepresentation.Type.PLANTUML,
                "workflow graph");

        System.out.println("\n\n");
        System.out.println(graphRepresentation.content());
        System.out.println("\n\n");

        return stateGraph;
    }

}
