package com.didiglobal.sds.client.enums;

public enum DowngradeActionType {
    VISIT(1, "访问量策略"), CONCURRENT(2, "并发策略"), EXCEPTION(3, "异常量策略"), EXCEPTION_RATE(4, "异常率策略"), TIMEOUT(5, "超时策略"),
    TOKEN_BUCKET(6, "令牌桶策略"), PRESSURE_TEST(7, "压测策略");

    private int type;

    private String desc;

    DowngradeActionType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DowngradeActionType{");
        sb.append("type=").append(type);
        sb.append(", desc='").append(desc).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
