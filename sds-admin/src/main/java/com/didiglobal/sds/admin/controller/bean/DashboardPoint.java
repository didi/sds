package com.didiglobal.sds.admin.controller.bean;

/**
 * @Description: 仪表盘数据上的一个点
 * @Author: manzhizhen
 * @Date: Create in 2019-09-13 17:15
 */
public class DashboardPoint {

    private Long timestamp;

    private Double value;

    public DashboardPoint() {
    }

    public DashboardPoint(Long timestamp, Double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DashboardPoint{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
