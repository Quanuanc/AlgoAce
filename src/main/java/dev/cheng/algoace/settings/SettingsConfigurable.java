package dev.cheng.algoace.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SettingsConfigurable implements Configurable {
    private SettingsUI settingsUI;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "AlgoAce";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return settingsUI.getPanel();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        settingsUI = new SettingsUI();
        return settingsUI.getPanel();
    }

    @Override
    public boolean isModified() {
        UserConfig settings = ApplicationManager.getApplication().getService(UserConfig.class);
        return !settingsUI.getCookieText().equals(settings.getLeetCodeCookie());
    }

    @Override
    public void apply() {
        UserConfig settings = ApplicationManager.getApplication().getService(UserConfig.class);
        settings.setLeetCodeCookie(settingsUI.getCookieText());
    }

    @Override
    public void reset() {
        UserConfig settings = ApplicationManager.getApplication().getService(UserConfig.class);
        settingsUI.setCookieText(settings.getLeetCodeCookie());
    }

    @Override
    public void disposeUIResources() {
        settingsUI = null;
    }
} 