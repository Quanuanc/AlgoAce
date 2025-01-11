package dev.cheng.algoace.model;

public class Solution {
    private final String questionId;
    private final String titleSlug;
    private final String typedCode;
    private final String referer;
    private final String submitUrl;
    private Integer submissionId;
    private String checkUrl;

    private Solution(SolutionBuilder builder) {
        this.questionId = builder.questionId;
        this.titleSlug = builder.titleSlug;
        this.typedCode = builder.typedCode;
        this.referer = builder.referer;
        this.submitUrl = builder.submitUrl;
        this.submissionId = builder.submissionId;
    }

    public static SolutionBuilder builder() {
        return new SolutionBuilder();
    }

    public void setSubmission(Integer submissionId) {
        this.submissionId = submissionId;
        this.checkUrl = CommonInfo.LC_BASE + "submissions/detail/" + submissionId + "/check/";
    }

    public String checkUrl() {
        return checkUrl;
    }

    public String questionId() {
        return questionId;
    }

    public String titleSlug() {
        return titleSlug;
    }

    public String typedCode() {
        return typedCode;
    }

    public String referer() {
        return referer;
    }

    public String submitUrl() {
        return submitUrl;
    }

    public Integer submissionId() {
        return submissionId;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "questionId='" + questionId + '\'' +
                ", titleSlug='" + titleSlug + '\'' +
                ", typedCode='" + typedCode + '\'' +
                ", referer='" + referer + '\'' +
                ", submitUrl='" + submitUrl + '\'' +
                ", submissionId=" + submissionId +
                ", checkUrl='" + checkUrl + '\'' +
                '}';
    }

    public static class SolutionBuilder {
        private String questionId;
        private String titleSlug;
        private String typedCode;
        private String referer;
        private String submitUrl;
        private Integer submissionId;

        private SolutionBuilder() {
        }

        public SolutionBuilder questionId(String questionFrontendId) {
            this.questionId = questionFrontendId;
            return this;
        }

        public SolutionBuilder titleSlug(String titleSlug) {
            this.titleSlug = titleSlug;
            return this;
        }

        public SolutionBuilder typedCode(String typedCode) {
            this.typedCode = typedCode;
            return this;
        }

        public SolutionBuilder referer(String referer) {
            this.referer = referer;
            return this;
        }

        public SolutionBuilder submitUrl(String submitUrl) {
            this.submitUrl = submitUrl;
            return this;
        }

        public SolutionBuilder submissionId(Integer submissionId) {
            this.submissionId = submissionId;
            return this;
        }

        public Solution build() {
            return new Solution(this);
        }
    }
}
