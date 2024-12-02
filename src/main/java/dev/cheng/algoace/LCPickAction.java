package dev.cheng.algoace;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import dev.cheng.algoace.model.CommonInfo;
import dev.cheng.algoace.service.LeetCodeService;
import dev.cheng.algoace.utils.FileManager;
import dev.cheng.algoace.utils.Notifier;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * LeetCode Pick Action
 */
public class LCPickAction extends DumbAwareAction {
    private final Icon questionMark = AllIcons.General.QuestionDialog;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        String inputId = Messages
                .showInputDialog(project, "Input a question number", CommonInfo.LC_PICK_TITLE, questionMark);
        if (inputId == null) {
            Notifier.warn(CommonInfo.LC_PICK_TITLE, "Input is null", project);
            return;
        }

        // check Solution.java exist or not in current project's leetcode path
        if (FileManager.checkSolutionExist(project, inputId)) {
            Notifier.info(CommonInfo.LC_PICK_TITLE, "q" + inputId + " already exists, opening for you", project);
            return;
        }

        int questionId = Integer.parseInt(inputId);
        LeetCodeService.getInstance().fetchQuestionById(questionId).thenAccept(question -> {
            if (question != null) {
                try {
                    FileManager.createSolutionFile(project, question);
                    Notifier.info(CommonInfo.LC_PICK_TITLE, String.format("Question %s: %s (%s)",
                            question.questionFrontendId(), question.title(), question.difficulty()), project);
                } catch (Exception ex) {
                    Notifier.error(CommonInfo.LC_PICK_TITLE, ex.getMessage(), project);
                }
            } else {
                Notifier.warn(CommonInfo.LC_PICK_TITLE, "Question not found", project);
            }
        }).exceptionally(throwable -> {
            Notifier.error(CommonInfo.LC_PICK_TITLE, "Error: " + throwable.getMessage(), project);
            return null;
        });


    }
}
