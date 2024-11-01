package dev.cheng.algoace

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAwareAction
import dev.cheng.algoace.utils.FileContentExtractor
import dev.cheng.algoace.utils.Notifier

class LeetCodePickAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val titleSlug = "two-sum"
        Notifier.info("LeetCode pick", "question: $titleSlug", project)
    }
}

class LeetCodeRunAction : DumbAwareAction() {
    private var logger = Logger.getInstance(LeetCodePickAction::class.java)
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return

        val questionNum = FileContentExtractor.extractQuestionNum(editor)
        val processedContent = FileContentExtractor.extractContent(editor)

        val notificationContent = String.format("Question: %s", questionNum)

        Notifier.info("LeetCode run", notificationContent, project)
    }
}

class LeetCodeSubmitAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        Notifier.info("LeetCode submit", "question: two-sum", project)
    }
}
