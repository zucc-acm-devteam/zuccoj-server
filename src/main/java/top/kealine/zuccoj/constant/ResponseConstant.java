package top.kealine.zuccoj.constant;

import top.kealine.zuccoj.util.BaseResponsePackageUtil;

import java.util.Map;

public class ResponseConstant {

    // User
    public static final Map<String, Object> X_USER_NOT_FOUND = BaseResponsePackageUtil.errorMessage("用户不存在", 404);
    public static final Map<String, Object> X_USER_LOGIN_FIRST = BaseResponsePackageUtil.errorMessage("请先登录");
    public static final Map<String, Object> X_USER_WRONG_PASSWORD = BaseResponsePackageUtil.errorMessage("用户名或密码错误");
    public static final Map<String, Object> X_USER_ALREADY_EXISTS = BaseResponsePackageUtil.errorMessage("用户已存在");

    public static final Map<String, Object> V_USER_LOGIN_SUCCESS = BaseResponsePackageUtil.succeedMessage("登录成功");
    public static final Map<String, Object> V_USER_LOGOUT_SUCCESS = BaseResponsePackageUtil.succeedMessage("退出成功");

    public static final Map<String, Object> X_ACCESS_DENIED = BaseResponsePackageUtil.errorMessage("访问受限", 403);

    // Upload
    public static final Map<String, Object> X_UPLOAD_FAILED = BaseResponsePackageUtil.errorMessage("上传失败");
    public static final Map<String, Object> V_UPLOAD_SUCCESS = BaseResponsePackageUtil.succeedMessage("上传成功");

    // Delete
    public static final Map<String, Object> X_DELETE_FAILED = BaseResponsePackageUtil.errorMessage("删除失败");
    public static final Map<String, Object> V_DELETE_SUCCESS = BaseResponsePackageUtil.succeedMessage("删除成功");


    // Solution
    public static final Map<String, Object> X_LANGUAGE_NOT_SUPPORTED = BaseResponsePackageUtil.errorMessage("该语言不支持");


    // Any
    public static final Map<String, Object> X_NOT_FOUND = BaseResponsePackageUtil.errorMessage("资源不存在", 404);
    public static final Map<String, Object> X_BAD_REQUEST = BaseResponsePackageUtil.errorMessage("请求参数错误", 400);


    // Judgehost
    public static final Map<String, Object> X_JUDGEHOST_DUE = BaseResponsePackageUtil.errorMessage("验证过期", 700);
}
