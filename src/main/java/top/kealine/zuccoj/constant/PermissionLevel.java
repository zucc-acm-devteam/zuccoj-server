package top.kealine.zuccoj.constant;

public class PermissionLevel {
    public static final int ANYONE = -999;
    public static final int FORBIDDEN = -1;
    public static final int COMMON = 0;
    public static final int DATA_VIEWER = 100;
    public static final int CODE_VIEWER = 200;
    public static final int ADMIN = 999;

    public static String toText(int status) {
        switch (status) {
            case FORBIDDEN: return "FORBIDDEN";
            case COMMON: return "COMMON";
            case DATA_VIEWER: return "DATA_VIEWER";
            case CODE_VIEWER: return "CODE_VIEWER";
            case ADMIN: return "ADMIN";
            default: return "UNKNOWN";
        }
    }
}
