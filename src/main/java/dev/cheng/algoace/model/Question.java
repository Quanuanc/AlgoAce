package dev.cheng.algoace.model;

import java.util.List;

public record Question(String questionId,
                       String questionFrontendId,
                       String title,
                       String titleSlug,
                       String difficulty,
                       List<QuestionCodeSnippet> codeSnippets
) {
}