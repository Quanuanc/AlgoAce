package dev.cheng.algoace;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import dev.cheng.algoace.model.CheckResult;
import dev.cheng.algoace.model.CommonInfo;
import dev.cheng.algoace.model.Solution;
import dev.cheng.algoace.service.LeetCodeService;
import dev.cheng.algoace.utils.FileManager;
import dev.cheng.algoace.utils.MiscUtils;
import dev.cheng.algoace.utils.Notifier;
import org.jetbrains.annotations.NotNull;

public class LCSubmitAction extends DumbAwareAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) {
            Notifier.warn(CommonInfo.LC_SUBMIT_TITLE, "No file is currently open", project);
            return;
        }
        FileManager.makeSureQuestionIdAndTitleSlug(project, editor);
        Solution solution = FileManager.getSolution(project, editor);

        ProgressManager.getInstance().run(new Task.Backgroundable(project, CommonInfo.LC_SUBMIT_TITLE, false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                try {
                    indicator.setIndeterminate(true);

                    Integer submissionId = LeetCodeService.getInstance().submitSolution(solution).get();
                    solution.setSubmission(submissionId);

                    CheckResult result = LeetCodeService.getInstance().checkSubmission(solution).get();

                    String title = result.status_msg();
                    if (result.status_code() == 10) {
                        String message = MiscUtils.generateSubmitResultMessage(result);
                        Notifier.runSuccess(title, message, project);
                    } else {
                        Notifier.runFailed(title, null, project);
                    }
                } catch (Exception ex) {
                    Notifier.error(CommonInfo.LC_SUBMIT_TITLE, ex.getMessage(), project);
                }
            }
        });
    }
}
