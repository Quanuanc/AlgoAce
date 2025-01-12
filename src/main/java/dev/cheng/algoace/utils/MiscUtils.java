package dev.cheng.algoace.utils;

import dev.cheng.algoace.model.CheckResult;
import dev.cheng.algoace.model.CommonInfo;

public class MiscUtils {
    public static String generateSubmitResultMessage(CheckResult checkResult) {
        int lenDiff = checkResult.status_runtime().length() - checkResult.status_memory().length();
        String statusRuntime;
        String statusMemory;
        if (lenDiff > 0) {
            statusMemory = checkResult.status_memory() + " ".repeat(lenDiff);
            statusRuntime = checkResult.status_runtime();
        } else if (lenDiff < 0) {
            statusRuntime = checkResult.status_runtime() + " ".repeat(-lenDiff);
            statusMemory = checkResult.status_memory();
        } else {
            statusRuntime = checkResult.status_runtime();
            statusMemory = checkResult.status_memory();
        }
        return String.format(CommonInfo.SUBMIT_RESULT_TEMPLATE,
                statusRuntime, checkResult.runtime_percentile(),
                statusMemory, checkResult.memory_percentile());
    }
}
