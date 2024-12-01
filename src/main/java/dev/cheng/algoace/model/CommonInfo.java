package dev.cheng.algoace.model;

public class CommonInfo {
    public static final String LC_BASE = "https://leetcode.com/";
    public static final String LC_API = LC_BASE + "graphql/";
    public static final String LC_API_PROBLEM = LC_BASE + "problems/";

    public static final String LC_PICK_TITLE = "LeetCode pick";
    public static final String LC_RUN_TITLE = "LeetCode run";
    public static final String LC_SUBMIT_TITLE = "LeetCode submit";

    public static final String SOURCE_BASE_URL = "src/main/java";
    public static final String USER_SOURCE_URL = "dev/cheng/leetcode";
    public static final String USER_SOURCE_PACKAGE = USER_SOURCE_URL.replace('/', '.');
    public static final String CODE_BEGIN = "// ------ AlgoAce Begin -----";
    public static final String CODE_END = "// ------ AlgoAce End -------";
    public static final String USER_CODE_TEMPLATE = """
            package {package};
            // {{questionFrontendId}} {{questionTitleSlug}}
            
            /**
             * @link <a href="{questionUrl}">{questionTitle}</a>
             */
            {codeBegin}
            {questionCode}
            {codeEnd}
            """;
    public static final String LC_QUESTION_LIST_QUERY = """
            {
              "query": "query questionList($categorySlug: String, $limit: Int, $skip: Int, $filters: QuestionListFilterInput) {\\n  questionList(\\n    categorySlug: $categorySlug\\n    limit: $limit\\n    skip: $skip\\n    filters: $filters\\n  ) {\\n    total: totalNum\\n    data {\\n      questionId\\n      questionFrontendId\\n      title\\n      titleSlug\\n      difficulty\\n      codeSnippets {\\n        lang\\n        langSlug\\n        code\\n      }\\n      exampleTestcases\\n      sampleTestCase\\n      topicTags {\\n        id\\n        name\\n        slug\\n      }\\n   }\\n  }\\n}",
              "variables": { "categorySlug": "", "limit": 1, "skip": %d, "filters": {} }
            }
            """;
}
