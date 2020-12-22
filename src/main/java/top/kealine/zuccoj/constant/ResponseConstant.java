package top.kealine.zuccoj.constant;

import top.kealine.zuccoj.util.BaseResponsePackageUtil;

import java.util.Map;

public class ResponseConstant {

    // User
    public static final Map<String, Object> X_USER_NOT_FOUND = BaseResponsePackageUtil.errorMessage("用户不存在", 404);
    public static final Map<String, Object> X_USER_LOGIN_FIRST = BaseResponsePackageUtil.errorMessage("请先登录");
    public static final Map<String, Object> X_USER_WRONG_PASSWORD = BaseResponsePackageUtil.errorMessage("用户名或密码错误");

    public static final Map<String, Object> V_USER_LOGIN_SUCCESS = BaseResponsePackageUtil.succeedMessage("登录成功");
    public static final Map<String, Object> V_USER_LOGOUT_SUCCESS = BaseResponsePackageUtil.succeedMessage("退出成功");

    public static final Map<String, Object> X_ACCESS_DENIED = BaseResponsePackageUtil.errorMessage("访问受限", 403);
}
