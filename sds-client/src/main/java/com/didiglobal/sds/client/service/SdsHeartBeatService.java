/**
 *
 */
package com.didiglobal.sds.client.service;

import com.alibaba.fastjson.JSON;
import com.didiglobal.sds.client.bean.HeartBeatResponse;
import com.didiglobal.sds.client.bean.HeartbeatRequest;
import com.didiglobal.sds.client.bean.SdsCycleInfo;
import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.counter.PowerfulCycleTimeCounter;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.client.util.*;
import org.slf4j.Logger;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import static com.didiglobal.sds.client.contant.BizConstant.BUCKET_TIME;
import static com.didiglobal.sds.client.contant.BizConstant.CYCLE_BUCKET_NUM;

/**
 * 心跳线程服务类 用于客户端上传数据和从服务端拉取最新的降级配置
 *
 * @author manzhizhen
 * @version $Id: SdsHeartBeatService.java, v 0.1 2016年2月25日 下午11:00:43
 */
final public class SdsHeartBeatService {

    /**
     * 应用组名称，同一个应用组下appName不能重复
     */
    private String appGroupName;

    /**
     * 应用名称，如bh-order
     */
    private String appName;

    /**
     * 服务端url list，便于HA和Load Balance
     */
    private final static List<String> SERVER_UPLOAD_URL_LIST = new ArrayList<>();
    private final static List<String> SERVER_PULL_URL_LIST = new ArrayList<>();

    /**
     * 当前所选的服务端url索引，默认从0开始
     */
    private int currentUploadUrlIndex = 0;
    private int currentPullUrlIndex = 0;

    /**
     * 修改的版本,用于标识是否有更新
     */
    private Long version = 0L;

    private Logger logger = SdsLoggerFactory.getDefaultLogger();

    private final static String UPLOAD_HEARTBEAT_PATH = "sds/heartbeat/add";
    private final static String PULL_POINT_STRATEGY_PATH = "sds/heartbeat/pullstrategy";

    /**
     * 单例
     */
    private static volatile SdsHeartBeatService instance = null;

    public static void createOnlyOne(String appGroupName, String appName, String serverAddrList) {
        if (instance == null) {
            synchronized (SdsHeartBeatService.class) {
                if (instance == null) {
                    instance = new SdsHeartBeatService(appGroupName, appName, serverAddrList);

                    instance.updatePointStrategyFromWebServer();
                }
            }
        }
    }

    public static SdsHeartBeatService getInstance() {
        return instance;
    }

    private SdsHeartBeatService(String appGroupName, String appName, String serverAddrList) {
        AssertUtil.notBlack(appGroupName, "appGroupName不能为空！");
        AssertUtil.notBlack(appName, "appName不能为空！");
        AssertUtil.notBlack(serverAddrList, "serverAddrList不能为空！");

        this.appGroupName = appGroupName;
        this.appName = appName;

        buildServerUrl(serverAddrList);
    }

    /**
     * 心跳：汇报本地的统计信息
     * 每个周期一次
     */
    public void uploadHeartbeatData() {
        try {
            Date now = new Date();

            Map<String, SdsCycleInfo> pointInfoMap = buildPointCycleInfo(now.getTime());

            HeartbeatRequest clientRequest = new HeartbeatRequest();
            Map<String, Object> param = new HashMap<>();
            clientRequest.setAppGroupName(appGroupName);
            clientRequest.setAppName(appName);
            clientRequest.setIp(IpUtils.getIp());
            clientRequest.setHostname(IpUtils.getHostname());
            clientRequest.setStatisticsCycleTime(new Date(DateUtils.getLastCycleEndTime(CYCLE_BUCKET_NUM * BUCKET_TIME,
                    now.getTime())));
            clientRequest.setPointInfoMap(pointInfoMap);
            param.put("client", JSON.toJSONString(clientRequest));

            logger.info("SdsHeartBeatService#uploadHeartbeatData 客户端请求参数：" + JSON.toJSONString(param));

            String body = null;
            try {
                body = HttpUtils.post(getCurUploadUrl(), param);
                logger.info("SdsHeartBeatService#uploadHeartbeatData 服务端应答：" + body);

            } catch (Exception e) {
                /**
                 * 抛异常则下次选取另一个地址
                 */
                String curUrl = getCurUploadUrl();
                String nextUrl = getNextUploadUrl();
                logger.warn("SdsHeartBeatService#uploadHeartbeatData 请求服务端（" + curUrl + "）异常：" +
                        body + ", 切换服务端地址为：" + nextUrl, e);
            }

        } catch (Throwable t) {
            logger.warn("SdsHeartBeatService#uploadHeartbeatData 心跳任务异常，可能无法及时上传数据", t);
        }
    }

    /**
     * 每5秒从服务端拉取最新的降级点策略配置
     */
    public void updatePointStrategyFromWebServer() {

        Map<String, Object> param = new HashMap<>();
        HeartbeatRequest clientRequest = new HeartbeatRequest();
        clientRequest.setAppGroupName(appGroupName);
        clientRequest.setAppName(appName);
        clientRequest.setIp(IpUtils.getIp());
        clientRequest.setHostname(IpUtils.getHostname());
        clientRequest.setVersion(version);

        /**
         * 这里先获取客户端用到的所有降级点
         */
        Enumeration<String> keys = SdsPowerfulCounterService.getInstance().getPointCounterMap().keys();
        List<String> pointList = new ArrayList<>();
        while(keys.hasMoreElements()) {
            pointList.add(keys.nextElement());
        }
        clientRequest.setPointList(pointList);

        param.put("client", JSON.toJSONString(clientRequest));

        HeartBeatResponse response = null;
        String body = null;
        try {
            body = HttpUtils.post(getCurPullUrl(), param);
            logger.info("SdsHeartBeatService#updatePointStrategyFromWebServer 服务端应答：" + body);

            if (StringUtils.isNotBlank(body)) {
                response = JSON.parseObject(body, HeartBeatResponse.class);

            } else {
                logger.warn("SdsHeartBeatService#updatePointStrategyFromWebServer 服务端应答为空，请求参数："
                        + JSON.toJSONString(param));
                return;
            }

        } catch (Exception e) {
            /**
             * 抛异常则下次选取另一个地址
             */
            String curUrl = getCurPullUrl();
            String nextUrl = getNextPullUrl();
            logger.warn("SdsHeartBeatService#updatePointStrategyFromWebServer 请求服务端（" + curUrl + "）异常：" +
                    body + ", 切换服务端地址为：" + nextUrl, e);
        }

        if (response == null) {
            logger.warn("SdsHeartBeatService#updatePointStrategyFromWebServer 服务端应答转JSON为null，请求参数："
                    + JSON.toJSONString(param));
            return;
        }

        if (StringUtils.isNotBlank(response.getErrorMsg())) {
            logger.warn("SdsHeartBeatService#updatePointStrategyFromWebServer 服务端有错误信息：" + response.getErrorMsg());
            return;
        }

        // 如果没有更新，直接返回
        if (response.isChanged() == null || !response.isChanged()) {
            logger.info("SdsHeartBeatService#updatePointStrategyFromWebServer 版本号没变，无需更新");
            return;
        }

        String sdsSchemeName = response.getSdsSchemeName();
        version = response.getVersion();

        ConcurrentHashMap<String, SdsStrategy> strategies = new ConcurrentHashMap<>();

        if (response.getStrategies() != null && response.getStrategies().size() > 0) {
            for (SdsStrategy strategy : response.getStrategies()) {
                strategies.put(strategy.getPoint(), strategy);
            }
        }

        /**
         * 更新降级点信息
         */
        resetPointInfo(strategies);

        logger.info("SdsHeartBeatService#updatePointStrategyFromWebServer 重设降级点参数成功，当前降级预案：" + sdsSchemeName);
    }

    /**
     * 将以逗号为分隔符的url地址放在urlList中
     *
     * @param serverAddrList
     */
    private void buildServerUrl(String serverAddrList) {
        String[] urlArray = serverAddrList.split(",");
        for (String url : urlArray) {
            if (StringUtils.isBlank(url)) {
                continue;
            }

            url = url.trim();

            SERVER_UPLOAD_URL_LIST.add(url.endsWith("/") ? url + UPLOAD_HEARTBEAT_PATH : url + "/" +
                    UPLOAD_HEARTBEAT_PATH);
            SERVER_PULL_URL_LIST.add(url.endsWith("/") ? url + PULL_POINT_STRATEGY_PATH : url + "/" +
                    PULL_POINT_STRATEGY_PATH);
        }

        if (SERVER_UPLOAD_URL_LIST.isEmpty()) {
            logger.error("SdsHeartBeatService 服务端地址列表为空，请设置服务端地址列表！");
            return;
        }

        /**
         * 通过随机打乱的方式来实现LB
         */
        Collections.shuffle(SERVER_UPLOAD_URL_LIST);
        Collections.shuffle(SERVER_PULL_URL_LIST);

        logger.info("SdsHeartBeatService 新增Upload服务端地址列表：" + JSON.toJSONString(SERVER_UPLOAD_URL_LIST));
        logger.info("SdsHeartBeatService 新增Pull服务端地址列表：" + JSON.toJSONString(SERVER_PULL_URL_LIST));
    }

    /**
     * 获取当前的服务端upload url地址
     * @return
     */
    private String getCurUploadUrl() {
        return SERVER_UPLOAD_URL_LIST.get(currentUploadUrlIndex);
    }

    /**
     * 获取当前的服务端pull url地址
     * @return
     */
    private String getCurPullUrl() {
        return SERVER_PULL_URL_LIST.get(currentPullUrlIndex);
    }

    /**
     * 获取下一个“可用”的服务端upload url地址
     * @return
     */
    private String getNextUploadUrl() {
        currentUploadUrlIndex = ++currentUploadUrlIndex % SERVER_UPLOAD_URL_LIST.size();
        return SERVER_UPLOAD_URL_LIST.get(currentUploadUrlIndex);
    }

    /**
     * 获取下一个“可用”的服务端pull url地址
     */
    private String getNextPullUrl() {
        currentPullUrlIndex = ++currentPullUrlIndex % SERVER_PULL_URL_LIST.size();
        return SERVER_PULL_URL_LIST.get(currentPullUrlIndex);
    }

    private void resetPointInfo(ConcurrentHashMap<String, SdsStrategy> strategyMap) {

        /**
         * 重设降级点策略
         */
        SdsStrategyService.getInstance().resetAll(strategyMap);

        /**
         * 重设降级点返回值
         */
        SdsDowngradeReturnValueService.getInstance().reset(strategyMap);
    }

    /**
     * 根据当前时间获取所有降级点上一完整周期的统计信息
     *
     * @param time
     * @result
     */
    private Map<String, SdsCycleInfo> buildPointCycleInfo(long time) {
        Map<String, SdsCycleInfo> map = new HashMap<>();

        // 统计访问量降级点信息
        for (Entry<String, PowerfulCycleTimeCounter> entry :
                SdsPowerfulCounterService.getInstance().getPointCounterMap().entrySet()) {
            String point = entry.getKey();
            PowerfulCycleTimeCounter counter = entry.getValue();

            SdsCycleInfo info = new SdsCycleInfo();
            info.setPoint(point);
            info.setVisitNum(counter.getLastCycleVisitValue(time));
            info.setExceptionNum(counter.getLastCycleExceptionValue(time));
            info.setConcurrentNum(counter.getLastCycleConcurrentValue(time));
            info.setDowngradeNum(counter.getLastCycleDowngradeValue(time));
            info.setTimeoutNum(counter.getLastCycleTimeoutValue(time));
            map.put(point, info);
        }

        return map;
    }
}
