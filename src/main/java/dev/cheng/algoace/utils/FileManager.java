package dev.cheng.algoace.utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import dev.cheng.algoace.exception.AlgoAceException;
import dev.cheng.algoace.model.CommonInfo;
import dev.cheng.algoace.model.Question;
import dev.cheng.algoace.model.QuestionCodeSnippet;
import dev.cheng.algoace.model.Solution;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileManager {

    private static final Pattern idSlugPattern = Pattern.compile("\\{(\\d+)}\\s*\\{([^}]*)}");
    private static final Pattern codePattern =
            Pattern.compile(CommonInfo.CODE_BEGIN + "\\s*(.*?)\\s*" + CommonInfo.CODE_END, Pattern.DOTALL);

    public static boolean checkSolutionExist(Project project, String questionId) {
        String basePath = project.getBasePath();
        if (basePath == null) return false;
        String directory =
                basePath + "/" + CommonInfo.SOURCE_BASE_URL + "/" + CommonInfo.USER_SOURCE_URL + "/" + "q" + questionId;
        return checkFileExists(project, directory, "Solution.java");
    }

    public static boolean checkFileExists(Project project, String directory, String fileName) {
        if (project == null || project.isDisposed()) return false;

        VirtualFileManager fileManager = VirtualFileManager.getInstance();
        String filePath = "file://" + directory + "/" + fileName;
        VirtualFile virtualFile = fileManager.findFileByUrl(filePath);

        boolean existed = virtualFile != null && !virtualFile.isDirectory() && virtualFile.exists();
        if (existed) {
            ApplicationManager.getApplication().invokeLater(() -> FileEditorManager.getInstance(project).openFile(virtualFile, true));
        }
        return existed;
    }

    public static void createSolutionFile(Project project, Question question) {
        if (project == null || project.isDisposed()) return;

        try {
            String basePath = project.getBasePath();
            String questionDir = basePath + "/" + CommonInfo.SOURCE_BASE_URL + "/" + CommonInfo.USER_SOURCE_URL + "/q"
                    + question.questionFrontendId();
            Path directoryPath = Paths.get(questionDir);

            Files.createDirectories(directoryPath);
            String fileContent = fileContent(question);

            Path solutionPath = directoryPath.resolve("Solution.java");
            Files.writeString(solutionPath, fileContent, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);

            String filePath = "file://" + solutionPath.toAbsolutePath();
            VirtualFile virtualFile = VirtualFileManager.getInstance().refreshAndFindFileByUrl(filePath);
            if (virtualFile != null) {
                ApplicationManager.getApplication().invokeLater(() -> FileEditorManager.getInstance(project).openFile(virtualFile, true));
            }
        } catch (IOException e) {
            throw new AlgoAceException("Failed to create Solution.java: " + e.getMessage());
        }
    }

    private static String fileContent(Question question) {
        String javaCode =
                question.codeSnippets().stream().filter(snippet -> "java".equals(snippet.langSlug()))
                        .findFirst()
                        .map(QuestionCodeSnippet::code)
                        .orElseThrow(() -> new AlgoAceException("No Java code snippet found"));
        String packageName = CommonInfo.USER_SOURCE_PACKAGE + ".q" + question.questionFrontendId();
        return CommonInfo.USER_CODE_TEMPLATE
                .replace("{package}", packageName)
                .replace("{questionFrontendId}", question.questionFrontendId())
                .replace("{questionTitleSlug}", question.titleSlug())
                .replace("{questionTitle}", question.title())
                .replace("{questionUrl}", CommonInfo.LC_API_PROBLEM + question.titleSlug())
                .replace("{codeBegin}", CommonInfo.CODE_BEGIN)
                .replace("{codeEnd}", CommonInfo.CODE_END)
                .replace("{questionCode}", javaCode);
    }

    public static Solution getSolution(String content) {
        String secondLine = content.lines()
                .skip(1)
                .findFirst()
                .orElseThrow();
        Matcher idSlugMatcher = idSlugPattern.matcher(secondLine);
        String id, titleSlug, code;
        if (idSlugMatcher.find()) {
            id = idSlugMatcher.group(1);
            titleSlug = idSlugMatcher.group(2);
        } else {
            throw new AlgoAceException("No id and title slug found");
        }
        Matcher codeMatcher = codePattern.matcher(content);
        if (codeMatcher.find()) {
            code = codeMatcher.group(1);
        } else {
            throw new AlgoAceException("No code found");
        }
        String referer = CommonInfo.LC_API_PROBLEM + titleSlug;
        String submitUrl = CommonInfo.LC_API_PROBLEM + titleSlug + "/submit/";
        return Solution.builder()
                .questionFrontendId(id)
                .titleSlug(titleSlug)
                .typedCode(code)
                .referer(referer)
                .submitUrl(submitUrl)
                .build();
    }
}
