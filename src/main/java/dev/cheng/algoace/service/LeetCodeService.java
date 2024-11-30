package dev.cheng.algoace.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.util.io.HttpRequests;
import dev.cheng.algoace.model.Question;
import dev.cheng.algoace.model.QuestionListRoot;
import dev.cheng.algoace.model.Solution;
import dev.cheng.algoace.model.Submission;
import dev.cheng.algoace.utils.CommonInfo;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class LeetCodeService {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static CompletableFuture<Question> fetchQuestionById(int questionId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String query = String.format(CommonInfo.LC_QUESTION_LIST_QUERY, questionId - 1);
                String response = HttpRequests.post(CommonInfo.LC_API, "application/json").connect(request -> {
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

    public static CompletableFuture<Integer> submitSolution(Solution solution) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 构建提交请求的JSON数据
                HashMap<String, String> submitData = new HashMap<>();
                submitData.put("lang", "java");
                submitData.put("question_id", solution.questionFrontendId());
                submitData.put("typed_code", solution.typedCode());

                String jsonBody = mapper.writeValueAsString(submitData);

                String titleSlug = solution.titleSlug();
                String submitUrl = CommonInfo.LC_API_PROBLEM + titleSlug + "/submit/";
                String referer = CommonInfo.LC_API_PROBLEM + titleSlug;

                // 发送POST请求
                String response = HttpRequests.post(submitUrl, HttpRequests.JSON_CONTENT_TYPE).tuner(request -> {
                    request.setRequestProperty("Origin", CommonInfo.LC_BASE);
                    request.setRequestProperty("Referer", referer);
                    // 这里需要添加用户的认证信息
                    request.setRequestProperty("Cookie", CommonInfo.LC_COOKIE);
                    request.setRequestProperty("x-csrftoken", CommonInfo.LC_X_CSRFTOKEN);
                }).connect(request -> {
                    request.write(jsonBody.getBytes());
                    return request.readString();
                });

                Submission submission = mapper.readValue(response, Submission.class);
                return submission.submission_id();
            } catch (Exception e) {
                throw new RuntimeException("Failed to submit solution: " + e.getMessage());
            }
        });
    }
} 