package dev.cheng.algoace;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import dev.cheng.algoace.model.Solution;
import dev.cheng.algoace.service.LeetCodeService;
import dev.cheng.algoace.utils.CommonInfo;
import dev.cheng.algoace.utils.Notifier;
import org.jetbrains.annotations.NotNull;

public class LCSubmitAction extends DumbAwareAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Notifier.info(CommonInfo.LC_SUBMIT_TITLE, "message", project);

        Solution solution = new Solution("10",
                "regular-expression-matching",
                "class Solution {\n    public boolean isMatch(String s, String p) {\n        return true;\n    }\n}"
        );

        LeetCodeService.submitSolution(solution).thenAccept(
                response -> Notifier.info(CommonInfo.LC_SUBMIT_TITLE, response.toString(), project)
        );
    }
}
