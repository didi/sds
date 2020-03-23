/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2015 All Rights Reserved.
 */
package com.didiglobal.sds.admin.exception;

import com.didiglobal.sds.admin.constants.SdsCode;
import org.apache.commons.lang3.StringUtils;


/**
 * @author manzhizhen
 * @version $Id: SdsBizException.java, v 0.1 2015年9月17日 下午4:50:57 Administrator Exp $
 */
public class SdsBizException extends RuntimeException {

    private static final long serialVersionUID = -541401709367656137L;
    /**
     * 代码
     *
     * @link SdsCode
     */
    private final Integer errorCode;
    /**
     * 异常信息
     */
    private final String msg;

    public SdsBizException(Integer code, String msg) {
        super();
        if (code == null) {
            this.errorCode = SdsCode.SYSTEM_ERROR.getCode();
        } else {
            this.errorCode = code;
        }
        this.msg = msg;
    }

    public SdsBizException(SdsCode paramError) {
        if (paramError == null) {
            this.errorCode = SdsCode.SYSTEM_ERROR.getCode();
            this.msg = SdsCode.SYSTEM_ERROR.getMsg();
        } else {
            this.errorCode = paramError.getCode();
            this.msg = paramError.getMsg();
        }
    }

    public SdsBizException(SdsCode paramError, Throwable e) {
        super(e);

        if (paramError == null) {
            this.errorCode = SdsCode.SYSTEM_ERROR.getCode();
            this.msg = SdsCode.SYSTEM_ERROR.getMsg();
        } else {
            this.errorCode = paramError.getCode();
            this.msg = paramError.getMsg();
        }
    }

    public SdsBizException(Integer code, String msg, Throwable e) {
        super(e);
        this.errorCode = code;
        this.msg = msg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return msg;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SdsBizException{");
        sb.append("errorCode=").append(errorCode);
        sb.append(", msg='").append(msg).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public String getMessage() {
        if (StringUtils.isBlank(msg)) {
            return super.getMessage();
        } else {
            return msg;
        }
    }
}
