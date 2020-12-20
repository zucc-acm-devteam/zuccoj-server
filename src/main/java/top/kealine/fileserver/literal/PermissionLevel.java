package top.kealine.fileserver.literal;

import top.kealine.fileserver.util.BaseResponsePackageUtil;

import java.util.Map;

public class PermissionLevel {
    public static final int ANYONE = -999;
    public static final int FORBIDDEN = -1;
    public static final int COMMON = 0;
    public static final int ADMIN = 999;

    public static final Map<String, Object> ACCESS_DENIED = BaseResponsePackageUtil.errorMessage("ACCESS DENIED", 403);
}
