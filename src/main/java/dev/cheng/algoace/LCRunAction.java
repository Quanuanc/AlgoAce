package dev.cheng.algoace;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import dev.cheng.algoace.model.Info;
import dev.cheng.algoace.utils.Notifier;
import org.jetbrains.annotations.NotNull;

public class LCRunAction extends DumbAwareAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Notifier.info(Info.LC_RUN_TITLE, "message", project);
    }
}
