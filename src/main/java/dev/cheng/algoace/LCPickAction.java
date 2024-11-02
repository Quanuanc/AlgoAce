package dev.cheng.algoace;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import dev.cheng.algoace.service.LeetCodeService;
import dev.cheng.algoace.utils.CommonInfo;
import dev.cheng.algoace.utils.Notifier;
import org.jetbrains.annotations.NotNull;

public class LCPickAction extends DumbAwareAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        String inputId = Messages.showInputDialog(project, "Input a question number", CommonInfo.LC_PICK_TITLE, Messages.getQuestionIcon());
        if (inputId == null) {
            Notifier.warn(CommonInfo.LC_PICK_TITLE, "Input is null", project);
            return;
        }

        try {
            int questionId = Integer.parseInt(inputId);
            LeetCodeService.fetchQuestionById(questionId).thenAccept(question -> {
                if (question != null) {
                    Notifier.info(CommonInfo.LC_PICK_TITLE, String.format("Question %s: %s (%s)", question.questionFrontendId(), question.title(), question.difficulty()), project);
                } else {
                    Notifier.warn(CommonInfo.LC_PICK_TITLE, "Question not found", project);
                }
            }).exceptionally(throwable -> {
                Notifier.error(CommonInfo.LC_PICK_TITLE, "Error: " + throwable.getMessage(), project);
                return null;
            });
        } catch (NumberFormatException ex) {
            Notifier.error(CommonInfo.LC_PICK_TITLE, "Invalid question number", project);
        }
    }
}
