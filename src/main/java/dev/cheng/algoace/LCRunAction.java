package dev.cheng.algoace;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import dev.cheng.algoace.model.CommonInfo;
import dev.cheng.algoace.utils.Notifier;
import org.jetbrains.annotations.NotNull;

public class LCRunAction extends DumbAwareAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();

        if (project == null) return;

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) {
            Notifier.warn(CommonInfo.LC_SUBMIT_TITLE, "No file is currently open", project);
            return;
        }

        PsiFile psiFile = PsiManager.getInstance(project).findFile(editor.getVirtualFile());
        if (!(psiFile instanceof PsiJavaFile javaFile)) {
            Notifier.warn(CommonInfo.LC_RUN_TITLE, "Not a Java file", project);
            return;
        }

        PsiClass[] classes = javaFile.getClasses();
        PsiClass solutionClass = null;

        for (PsiClass aClass : classes) {
            if ("Solution".equals(aClass.getName())) {
                solutionClass = aClass;
                break;
            }
        }

        if (solutionClass == null) {
            Notifier.warn(CommonInfo.LC_RUN_TITLE, "No Solution class found", project);
            return;
        }

        String classText = solutionClass.getText();
        Notifier.info(CommonInfo.LC_RUN_TITLE, classText, project);
    }
}
