package dev.cheng.algoace.utils;

public class CommonInfo {
    public static final String LC_PICK_TITLE = "LeetCode pick";
    public static final String LC_RUN_TITLE = "LeetCode run";
    public static final String LC_SUBMIT_TITLE = "LeetCode submit";

    public static final String LC_QUESTION_LIST_QUERY = """
            {
                "query": "query questionList($categorySlug: String, $limit: Int, $skip: Int, $filters: QuestionListFilterInput) {\\n  questionList(\\n    categorySlug: $categorySlug\\n    limit: $limit\\n    skip: $skip\\n    filters: $filters\\n  ) {\\n    total: totalNum\\n    data {\\n      questionId\\n      questionFrontendId\\n      title\\n      titleSlug\\n      difficulty\\n      codeSnippets {\\n        lang\\n        langSlug\\n        code\\n      }\\n    }\\n  }\\n}\\n  ",
                "variables": {
                    "categorySlug": "",
                    "limit": 1,
                    "skip": %d,
                    "filters": {}
                }
            }
            """;
}