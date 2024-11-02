package dev.cheng.algoace.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.util.io.HttpRequests;
import dev.cheng.algoace.model.Question;
import dev.cheng.algoace.model.QuestionListRoot;
import dev.cheng.algoace.utils.CommonInfo;

import java.util.concurrent.CompletableFuture;

public class LeetCodeService {
    private static final String LC_API = "https://leetcode.com/graphql/";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static CompletableFuture<Question> fetchQuestionById(int questionId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String query = String.format(CommonInfo.LC_QUESTION_LIST_QUERY, questionId - 1);
                String response = HttpRequests.post(LC_API, "application/json")
                        .connect(request -> {
                            request.write(query.getBytes());
                            return request.readString();
                        });

                QuestionListRoot root = mapper.readValue(response, QuestionListRoot.class);
                return root.data().questionList().data().get(0);
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch question: " + e.getMessage());
            }
        });
    }
} 