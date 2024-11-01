package dev.cheng.algoace.utils

import com.intellij.openapi.editor.Editor

class FileContentExtractor {
    companion object {
        fun extractContent(editor: Editor): String {
            val document = editor.document
            return document.text.lines()
                .filterNot { it.trim().startsWith("package") }
                .joinToString("\n")
        }

        fun extractQuestionNum(editor: Editor): Int {
            val document = editor.document
            val content = document.text

            // 匹配两种可能的注释格式
            val patterns = listOf(
                Regex("/\\*\\*\\s*\n\\s*\\*\\s*(\\d+)\\..*"), // multi lines comments
                Regex("//\\s*\\[(\\d+)].*") // single line comments
            )

            patterns.forEach { pattern ->
                val matchResult = pattern.find(content)
                if (matchResult != null) {
                    return matchResult.groupValues[1].toInt()
                }
            }

            return -1
        }
    }
}