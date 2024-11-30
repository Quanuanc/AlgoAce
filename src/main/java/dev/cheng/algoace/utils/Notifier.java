package dev.cheng.algoace.utils;

import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public class Notifier {
    private static final NotificationGroup algoNotificationGroup =
            NotificationGroupManager.getInstance().getNotificationGroup("AlgoAce");

    public static void info(String title, String message, Project project) {
        algoNotificationGroup.createNotification(title, message, NotificationType.INFORMATION)
                .notify(project);
    }

    public static void warn(String title, String message, Project project) {
        algoNotificationGroup.createNotification(title, message, NotificationType.WARNING)
                .notify(project);
    }

    public static void error(String title, String message, Project project) {
        algoNotificationGroup.createNotification(title, message, NotificationType.ERROR)
                .notify(project);
    }
}
