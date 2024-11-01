package dev.cheng.algoace.utils

import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

class Notifier {
    companion object {
        private val algoNotificationGroup: NotificationGroup =
            NotificationGroupManager.getInstance().getNotificationGroup("AlgoAce")

        fun info(title: String, message: String, project: Project) {
            algoNotificationGroup.createNotification(title, message, NotificationType.INFORMATION).notify(project);
        }

    }
}