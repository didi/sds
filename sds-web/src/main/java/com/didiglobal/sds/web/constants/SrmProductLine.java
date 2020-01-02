package com.didiglobal.sds.web.constants;

import org.springframework.util.StringUtils;

/**
 * Created by tianyulei on 2019/6/16
 * srm 产品线ID
 **/
public enum SrmProductLine {

    DEFAULE_GROUP(0, "defaule", "默认应用组"),

    HEIMA(4358, "hm", "黑马"),

    HTW(4359, "htw", "海棠湾"),

    DAIJIA(157, "daijia", "代驾"),

    IOT(11038, "iot", "IOT"),
    ;

    private Integer code;

    private String appGroup;

    private String msg;

    SrmProductLine(Integer code, String appGroup, String msg) {
        this.code = code;
        this.appGroup = appGroup;
        this.msg = msg;
    }

    public static SrmProductLine valueOfCode(Integer code) {
        if (code == null) {
            return DEFAULE_GROUP;
        }
        for (SrmProductLine e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return DEFAULE_GROUP;
    }

    public static SrmProductLine valueOfAppGroup(String appGroup){
        if(StringUtils.isEmpty(appGroup)){
            return DEFAULE_GROUP;
        }

        for (SrmProductLine srm : values()){
            if(srm.getAppGroup().equals(appGroup)){
                return srm;
            }
        }
        return DEFAULE_GROUP;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getAppGroup() {
        return appGroup;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SrmProductLine{");
        sb.append("code=").append(code);
        sb.append(", appGroup='").append(appGroup).append('\'');
        sb.append(", msg='").append(msg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
