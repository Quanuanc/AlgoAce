package dev.cheng.algoace.utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import dev.cheng.algoace.model.Question;
import dev.cheng.algoace.model.QuestionCodeSnippet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileManager {
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
            // 构建目录路径
            String basePath = project.getBasePath();
            String questionDir = basePath + "/" + CommonInfo.SOURCE_BASE_URL + "/" + CommonInfo.USER_SOURCE_URL + "/q"
                    + question.questionFrontendId();
            Path directoryPath = Paths.get(questionDir);

            // 创建目录
            Files.createDirectories(directoryPath);

            // 获取Java代码片段
            String javaCode =
                    question.codeSnippets().stream().filter(snippet -> "java".equals(snippet.langSlug())).findFirst().map(QuestionCodeSnippet::code).orElseThrow(() -> new RuntimeException("No Java code snippet found"));

            // 替换模板中的占位符
            String packageName = CommonInfo.USER_SOURCE_PACKAGE + ".q" + question.questionFrontendId();
            String fileContent = CommonInfo.USER_CODE_TEMPLATE
                    .replace("{package}", packageName)
                    .replace("{questionFrontendId}", question.questionFrontendId())
                    .replace("{questionTitle}", question.title())
                    .replace("{questionCode}", javaCode);

            // 创建并写入文件
            Path solutionPath = directoryPath.resolve("Solution.java");
            Files.writeString(solutionPath, fileContent, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            // 刷新 IDE 的虚拟文件系统以显示新文件
            VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);

            // 获取虚拟文件并打开
            String filePath = "file://" + solutionPath.toAbsolutePath();
            VirtualFile virtualFile = VirtualFileManager.getInstance().refreshAndFindFileByUrl(filePath);
            if (virtualFile != null) {
                ApplicationManager.getApplication().invokeLater(() -> FileEditorManager.getInstance(project).openFile(virtualFile, true));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create Solution.java: " + e.getMessage());
        }
    }

}
