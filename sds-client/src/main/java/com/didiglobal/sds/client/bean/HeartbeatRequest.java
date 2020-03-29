/**
 *
 */
package com.didiglobal.sds.client.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户端心跳请求对象 客户端每分钟会往服务端发送一次心跳，用于上传客户端上一分钟数据并从服务端拉取最新的策略
 *
 * @author manzhizhen
 * @version $Id: HeartBeatRequest.java, v 0.1 2016年2月20日 上午9:47:01 Administrator
 * Exp $
 */
public class HeartbeatRequest {

    /**
     * 客户端服务器IP
     */
    private String ip;

    /**
     * 客户端服务器hostname
     */
    private String hostname;

    /**
     * 应用组名称，同一个应用组下appName不能重复
     */
    private String appGroupName;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 版本号
     */
    private Long version;

    /**
     * 统计时间，即访问量降级点、异常量降级点和异常率降级点的统计时间
     * 比如2018-09-14 01:10:10上报的数据表示01:10:00 至 01:10:10 之间的数据
     */
    private Date statisticsCycleTime;

    /**
     * 降级点信息Map
     * 上传心跳数据使用
     */
    private Map<String, SdsCycleInfo> pointInfoMap = new HashMap<>();

    /**
     * 客户端使用的降级点列表，用于从服务端拉取最新配置
     */
    private List<String> pointList;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getAppGroupName() {
        return appGroupName;
    }

    public void setAppGroupName(String appGroupName) {
        this.appGroupName = appGroupName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Date getStatisticsCycleTime() {
        return statisticsCycleTime;
    }

    public void setStatisticsCycleTime(Date statisticsCycleTime) {
        this.statisticsCycleTime = statisticsCycleTime;
    }

    public Map<String, SdsCycleInfo> getPointInfoMap() {
        return pointInfoMap;
    }

    public void setPointInfoMap(Map<String, SdsCycleInfo> pointInfoMap) {
        this.pointInfoMap = pointInfoMap;
    }

    public List<String> getPointList() {
        return pointList;
    }

    public void setPointList(List<String> pointList) {
        this.pointList = pointList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HeartbeatRequest{");
        sb.append("ip='").append(ip).append('\'');
        sb.append(", hostname='").append(hostname).append('\'');
        sb.append(", appGroupName='").append(appGroupName).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", version=").append(version);
        sb.append(", statisticsCycleTime=").append(statisticsCycleTime);
        sb.append(", pointInfoMap=").append(pointInfoMap);
        sb.append(", pointList=").append(pointList);
        sb.append('}');
        return sb.toString();
    }
}
