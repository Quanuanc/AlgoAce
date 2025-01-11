package dev.cheng.algoace.settings;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

public class SettingsUI {
    private final JPanel mainPanel;
    private final JTextArea cookieField;

    public SettingsUI() {
        // 创建主面板
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(JBUI.Borders.empty(2));

        // 创建内容面板
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(JBUI.Borders.emptyTop(2));

        // 创建多行文本区域
        cookieField = new JTextArea(3, 50);
        cookieField.setLineWrap(true);
        cookieField.setWrapStyleWord(true);

        // 添加滚动条
        JBScrollPane scrollPane = new JBScrollPane(cookieField);
        scrollPane.setPreferredSize(new Dimension(JBUI.scale(400), JBUI.scale(80)));

        // 创建标签
        JBLabel label = new JBLabel("LeetCode cookie:");
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setBorder(JBUI.Borders.empty(5, 0));

        // 创建cookie输入面板
        JPanel cookiePanel = new JPanel(new BorderLayout());
        cookiePanel.add(label, BorderLayout.NORTH);
        cookiePanel.add(scrollPane, BorderLayout.CENTER);

        // 组装所有组件
        contentPanel.add(cookiePanel, BorderLayout.NORTH);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public String getCookieText() {
        return cookieField.getText();
    }

    public void setCookieText(String cookie) {
        cookieField.setText(cookie);
    }
} 