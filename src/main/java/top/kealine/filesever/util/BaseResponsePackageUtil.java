package top.kealine.filesever.util;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class BaseResponsePackageUtil {

    public static Map<String, Object> errorMessage() {
        return errorMessage(null);
    }

    public static Map<String, Object> errorMessage(String msg) {
        if (msg==null || "".equals(msg)) {
            msg = "Unknown Error";
        }
        return ImmutableMap.of(
                "code", 0,
                "msg", msg
        );
    }

    public static Map<String, Object> succeedMessage() {
        return succeedMessage(null);
    }

    public static Map<String, Object> succeedMessage(String msg) {
        if (msg==null || "".equals(msg)) {
            msg = "Successful";
        }
        return ImmutableMap.of(
                "code", 200,
                "msg", msg
        );
    }

    public static Map<String, Object> baseData(Object data) {
        return ImmutableMap.of(
                "code", 200,
                "data", data
        );
    }
}
