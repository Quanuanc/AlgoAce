package dev.cheng.algoace.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Service
@State(name = "AlgoAce", storages = {@Storage("AlgoAce.xml")})
public final class UserConfig implements PersistentStateComponent<UserConfig.DataBean> {
    private DataBean data = new DataBean();

    @Override
    public @Nullable UserConfig.DataBean getState() {
        return data;
    }

    @Override
    public void loadState(@NotNull DataBean state) {
        this.data = state;
    }

    public String getLeetCodeCookie() {
        return data.leetCodeCookie;
    }

    public void setLeetCodeCookie(String cookie) {
        data.leetCodeCookie = cookie;
        data.leetCodeCsrfToken = Arrays.stream(cookie.split("; "))
                .filter(item -> item.startsWith("csrftoken="))
                .map(item -> item.substring(item.indexOf('=') + 1))
                .findFirst().orElse(null);
    }

    public String getLeetCodeCsrfToken() {
        return data.leetCodeCsrfToken;
    }

    public static class DataBean {
        public String leetCodeCookie = "";
        public String leetCodeCsrfToken = "";
    }
}
