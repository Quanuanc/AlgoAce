package dev.cheng.algoace.utils;

import com.intellij.openapi.ui.InputValidator;

public class PickInputValidator implements InputValidator {
    @Override
    public boolean checkInput(String inputString) {
        if (inputString == null || inputString.isEmpty()) {
            return false;
        }
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    @Override
    public boolean canClose(String inputString) {
        return true;
    }
}
