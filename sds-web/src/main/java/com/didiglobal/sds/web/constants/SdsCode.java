package com.didiglobal.sds.web.constants;

/**
 * Sds错误码枚举
 */
public enum SdsCode {

    SUCCESS(200, "成功"),

    PARAM_ERROR(1001, "参数错误"),

    SYSTEM_ERROR(1002, "系统错误"),

    DATA_CHECK_FAIL(1003, "数据校验失败，请检查数据完整性"),

    UNIQUE_KEY_IS_EXISTS(1004, "唯一约束校验失败，数据已经存在！"),

    DATA_ID_NOT_EXISTS(1005, "数据ID不存在！"),

    RATE_RANGE_CHECK_FAIL(1006, "降级比例取值范围是[0-100]！"),

    MODULE_CAN_NOT_DELETE(1007, "该模块下存在降级点，无法删除！");

    /**
      * 内部唯一标识
      */
    private final Integer code;
    /**
     * 信息
     */
    private final String  msg;

    SdsCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 返回错误码的code
     * @return 错误码的code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 返回错误码的描述
     * @return 错误码描述
     */
    public String getMsg() {
        return msg;
    }

    public static SdsCode valueOfCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (SdsCode e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;

    }
}
