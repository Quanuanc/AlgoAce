package dev.cheng.algoace.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.io.HttpRequests;
import dev.cheng.algoace.exception.AlgoAceException;
import dev.cheng.algoace.model.*;
import dev.cheng.algoace.settings.UserConfig;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class LeetCodeService {
    private static volatile LeetCodeService instance;
    private final ObjectMapper mapper = new ObjectMapper();
    private final UserConfig userConfig = ApplicationManager.getApplication().getService(UserConfig.class);

    private LeetCodeService() {
    }

    public static LeetCodeService getInstance() {
        if (instance == null) {
            synchronized (LeetCodeService.class) {
                if (instance == null) {
                    instance = new LeetCodeService();
                }
            }
        }
        return instance;
    }

    public CompletableFuture<Question> fetchQuestionById(int questionId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String query = String.format(CommonInfo.LC_QUESTION_LIST_QUERY, questionId - 1);
                String response =
                        HttpRequests.post(CommonInfo.LC_API, HttpRequests.JSON_CONTENT_TYPE).connect(request -> {
                            request.write(query.getBytes());
                            return request.readString();
                        });

                QuestionListRoot root = mapper.readValue(response, QuestionListRoot.class);
                return root.data().questionList().data().get(0);
            } catch (Exception e) {
                throw new AlgoAceException("Failed to fetch question: " + e.getMessage());
            }
        });
    }

    public CompletableFuture<Integer> submitSolution(Solution solution) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 构建提交请求的JSON数据
                HashMap<String, String> submitData = new HashMap<>();
                submitData.put("lang", "java");
                submitData.put("question_id", solution.questionFrontendId());
                submitData.put("typed_code", solution.typedCode());

                String jsonBody = mapper.writeValueAsString(submitData);

                // 发送POST请求
                String response =
                        HttpRequests.post(solution.submitUrl(), HttpRequests.JSON_CONTENT_TYPE).tuner(request -> {
                            request.setRequestProperty("Origin", CommonInfo.LC_BASE);
                            request.setRequestProperty("Referer", solution.referer());
                            // 这里需要添加用户的认证信息
                            request.setRequestProperty("Cookie", userConfig.getLeetCodeCookie());
                            request.setRequestProperty("x-csrftoken", userConfig.getLeetCodeCsrfToken());
                        }).connect(request -> {
                            request.write(jsonBody.getBytes());
                            return request.readString();
                        });

                SubmitResult submitResult = mapper.readValue(response, SubmitResult.class);
                return submitResult.submission_id();
            } catch (Exception e) {
                throw new AlgoAceException("Failed to submit solution: " + e.getMessage());
            }
        });
    }

    public CompletableFuture<CheckResult> checkSubmission(Solution solution) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                CheckResult result = null;
                int maxAttempts = 5;
                int attempt = 0;

                while (attempt < maxAttempts) {
                    String response = HttpRequests.request(solution.checkUrl())
                            .tuner(request -> {
                                request.setRequestProperty("Content-Type", HttpRequests.JSON_CONTENT_TYPE);
                                request.setRequestProperty("Cookie", userConfig.getLeetCodeCookie());
                                request.setRequestProperty("x-csrftoken", userConfig.getLeetCodeCsrfToken());
                                request.setRequestProperty("Referer", solution.referer());
                            })
                            .connect(request -> {
                                try (java.io.BufferedReader reader = new java.io.BufferedReader(
                                        new java.io.InputStreamReader(request.getInputStream(),
                                                StandardCharsets.UTF_8))) {
                                    StringBuilder responseBuilder = new StringBuilder();
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        responseBuilder.append(line);
                                    }
                                    return responseBuilder.toString();
                                }
                            });

                    result = mapper.readValue(response, CheckResult.class);

                    if ("SUCCESS".equals(result.state())) {
                        break;
                    }

                    attempt++;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new AlgoAceException("CheckSubmission interrupted: " + e.getMessage());
                    }
                }

                if ("PENDING".equals(result.state())) {
                    throw new AlgoAceException("CheckSubmission still pending");
                }

                return result;
            } catch (Exception e) {
                throw new AlgoAceException("Check submission failed: " + e.getMessage());
            }
        });
    }
} 