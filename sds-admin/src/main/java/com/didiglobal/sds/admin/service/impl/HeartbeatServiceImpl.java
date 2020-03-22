package com.didiglobal.sds.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.didiglobal.sds.client.bean.HeartBeatResponse;
import com.didiglobal.sds.client.bean.HeartbeatRequest;
import com.didiglobal.sds.client.bean.SdsCycleInfo;
import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.admin.dao.AppInfoDao;
import com.didiglobal.sds.admin.dao.HeartbeatDao;
import com.didiglobal.sds.admin.dao.PointReturnValueDao;
import com.didiglobal.sds.admin.dao.PointStrategyDao;
import com.didiglobal.sds.admin.dao.bean.AppInfoDO;
import com.didiglobal.sds.admin.dao.bean.HeartbeatDO;
import com.didiglobal.sds.admin.dao.bean.PointDictDO;
import com.didiglobal.sds.admin.dao.bean.PointReturnValueDO;
import com.didiglobal.sds.admin.dao.bean.PointStrategyDO;
import com.didiglobal.sds.admin.service.HeartbeatService;
import com.didiglobal.sds.admin.service.PointDictService;
import com.didiglobal.sds.admin.service.bean.HeartbeatBO;
import com.didiglobal.sds.admin.service.bean.HeartbeatShowBO;
import com.didiglobal.sds.admin.util.FastBeanUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by manzhizhen on 18/2/12.
 */
@Service("heartbeatService")
public class HeartbeatServiceImpl implements HeartbeatService {

    @Autowired
    private AppInfoDao appInfoDao;
    @Autowired
    private HeartbeatDao heartbeatDao;
    @Autowired
    private PointStrategyDao pointStrategyDao;
    @Autowired
    private PointReturnValueDao pointReturnValueDao;

    @Autowired
    private PointDictService pointDictService;

    private final long TWO_HOUR = 2 * 3600 * 1000;
    private final long ONE_DAY = 12 * TWO_HOUR;
    private final long TWO_DAY = 2 * ONE_DAY;

    private static Logger logger = SdsLoggerFactory.getHeartbeatLogger();

    @Override
    public HeartBeatResponse saveHeartbeatInfo(HeartbeatRequest heartbeatRequest) {

        if (heartbeatRequest == null || StringUtils.isBlank(heartbeatRequest.getAppGroupName()) ||
                StringUtils.isBlank(heartbeatRequest.getAppName())) {
            logger.error("HeartbeatServiceImpl#saveHeartbeatInfo 参数非法，heartbeatRequest:" + heartbeatRequest);
            return null;
        }

        if (heartbeatRequest.getVersion() == null) {
            heartbeatRequest.setVersion(-1L);
        }

        AppInfoDO appInfoDO = appInfoDao.queryAppInfo(heartbeatRequest.getAppGroupName(),
                heartbeatRequest.getAppName());
        if (appInfoDO == null) {
            logger.error("HeartbeatServiceImpl#saveHeartbeatInfo 非法appGroupName，appGroupName:" +
                    heartbeatRequest.getAppGroupName() + ", appName:" + heartbeatRequest.getAppName());
            return new HeartBeatResponse("非法appGroupName，appGroupName:" + heartbeatRequest.getAppGroupName() +
                    ", appName:" + heartbeatRequest.getAppName());
        }

        /**
         * 保存心跳数据
         */
        saveRecord(heartbeatRequest);

        return new HeartBeatResponse();
    }

    @Override
    public HeartBeatResponse checkAndGetNewestPointStrategy(HeartbeatRequest heartbeatRequest) {

        if (heartbeatRequest == null || StringUtils.isBlank(heartbeatRequest.getAppGroupName()) ||
                StringUtils.isBlank(heartbeatRequest.getAppName())) {
            logger.error("HeartbeatServiceImpl#checkAndGetNewestPointStrategy 参数非法，heartbeatRequest:" + heartbeatRequest);
            return null;
        }

        HeartBeatResponse response = new HeartBeatResponse();
        Long version = heartbeatRequest.getVersion();

        AppInfoDO appInfoDO = appInfoDao.queryAppInfo(heartbeatRequest.getAppGroupName(),
                heartbeatRequest.getAppName());
        if (appInfoDO == null) {
            logger.warn("HeartbeatServiceImpl#checkAndGetNewestPointStrategy appGroupName或appName不存在，appGroupName:" +
                    heartbeatRequest.getAppGroupName() +
                    ", appName:" + heartbeatRequest.getAppName());
            return new HeartBeatResponse("appGroupName或appName不存在:" + heartbeatRequest.getAppGroupName() +
                    ", appName:" + heartbeatRequest.getAppName());
        }

        /**
         * 如果服务端版本和客户端的版本一样，那么告知客户端无需改变
         */
        if (Objects.equals(version, appInfoDO.getVersion())) {
            response.setChanged(false);

        } else {
            response.setChanged(true);
            response.setVersion(appInfoDO.getVersion());

            /**
             * 查询这些降级点对应的降级策略
             */
            List<PointStrategyDO> pointStrategyDOList =
                    pointStrategyDao.queryPointStrategyBatch(heartbeatRequest.getAppGroupName(),
                            heartbeatRequest.getAppName(), null, appInfoDO.getSdsSchemeName());
            Map<String, SdsStrategy> pointMap = Maps.newHashMap();
            List<SdsStrategy> strategies = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(pointStrategyDOList)) {
                /**
                 * 只返回有效的并且和当前模块的告警降级预案匹配的降级点的信息
                 */
                for (PointStrategyDO strategyDO : pointStrategyDOList) {
                    SdsStrategy sdsStrategy = new SdsStrategy();
                    pointMap.put(strategyDO.getPoint(), sdsStrategy);
                    strategies.add(FastBeanUtil.copyForNew(strategyDO, sdsStrategy));
                }

                List<PointReturnValueDO> pointReturnValueList =
                        pointReturnValueDao.queryPointReturnValueBatch(heartbeatRequest.getAppGroupName(),
                                heartbeatRequest.getAppName(), Lists.newArrayList(pointMap.keySet()), 1);

                if (CollectionUtils.isNotEmpty(pointReturnValueList)) {
                    for (PointReturnValueDO pointReturnValueDO : pointReturnValueList) {
                        pointMap.get(pointReturnValueDO.getPoint()).setReturnValueStr(
                                pointReturnValueDO.getReturnValueStr());
                    }
                }

                response.setStrategies(strategies);
            }
        }

        response.setSdsSchemeName(appInfoDO.getSdsSchemeName());

        return response;
    }

    @Override
    public HeartbeatShowBO queryHeartbeatList(String appGroupName, String appName, String point, Date startTime,
                                              Date endTime) {

        HeartbeatShowBO result = new HeartbeatShowBO();
        result.setAppGroupName(appGroupName);
        result.setAppName(appName);
        result.setPoint(point);

        /**
         * 直接从数据库中查询的是多个机器（通过ip来区分）的统计结果，需要按照时间周期统计求和
         */
        List<HeartbeatDO> heartbeatDOList = heartbeatDao.queryHeartbeatList(appGroupName, appName, point, startTime,
                endTime);
        if (CollectionUtils.isEmpty(heartbeatDOList)) {
            return result;
        }

        /**
         * 页面展示的时间力度，最大粒度1小时一个点，最小粒度为10秒钟一个点
         * 注意：粒度由传入的startTime和endTime来判断，保证页面的处理效率
         */
        Long pageShowTimeCycleMilliSecond = calShowTimeCycle(startTime, endTime);

        /**
         * 先通过统计周期时间和机器ip来聚合数据库中的数据
         *
         * key-统计的周期时间， value-该周期内所有机器（ip）的统计数据的求和
         */
        Map<Date, HeartbeatBO> dbCycleDataMap = Maps.newTreeMap();
        heartbeatDOList.stream()
                .filter(heartbeatDO -> heartbeatDO.getStatisticsCycleTime() != null)
                .forEach(heartbeatDO -> {
                            HeartbeatBO totalHeartbeatBO = dbCycleDataMap.get(heartbeatDO.getStatisticsCycleTime());

                            if (totalHeartbeatBO == null) {
                                totalHeartbeatBO = new HeartbeatBO();
                                totalHeartbeatBO.setShowTime(heartbeatDO.getStatisticsCycleTime());
                                dbCycleDataMap.put(heartbeatDO.getStatisticsCycleTime(), totalHeartbeatBO);
                            }

                            mergeHeartbeatBO(totalHeartbeatBO, FastBeanUtil.copyForNew(heartbeatDO, new HeartbeatBO()));
                        }
                );


        /**
         * 根据展示的时间力度，来进一步聚合数据（如果时间粒度大于10秒，将对该时间粒度内的周期数据求均值）
         */
        long startShowTime = startTime.getTime() + (startTime.getTime() % pageShowTimeCycleMilliSecond == 0 ? 0 :
                pageShowTimeCycleMilliSecond - startTime.getTime() % pageShowTimeCycleMilliSecond);
        long endShowTime = endTime.getTime() + (endTime.getTime() % pageShowTimeCycleMilliSecond == 0 ? 0 :
                pageShowTimeCycleMilliSecond - endTime.getTime() % pageShowTimeCycleMilliSecond);
        List<HeartbeatBO> origiDataList = Lists.newArrayList(dbCycleDataMap.values());
        int origiDataListIndex = 0;
        List<HeartbeatBO> showList = Lists.newArrayList();
        for (long showtime = startShowTime; showtime <= endShowTime; showtime += pageShowTimeCycleMilliSecond) {
            HeartbeatBO showHeartbeatBO = new HeartbeatBO();
            showHeartbeatBO.setShowTime(new Date(showtime));

            showList.add(showHeartbeatBO);

            int cycleNum = 0;
            while (origiDataListIndex < origiDataList.size()) {
                HeartbeatBO dbHeartbeatBO = origiDataList.get(origiDataListIndex);

                if (dbHeartbeatBO.getShowTime().getTime() > showtime) {
                    break;
                }

                mergeHeartbeatBO(showHeartbeatBO, dbHeartbeatBO);

                cycleNum++;
                origiDataListIndex++;
            }

            /**
             * 如果一个展示时间周期中包含多个数据统计周期，那么需要取均值
             */
            showHeartbeatBO.setVisitNum(showHeartbeatBO.getVisitNum() / cycleNum);
            showHeartbeatBO.setExceptionNum(showHeartbeatBO.getExceptionNum() / cycleNum);
            showHeartbeatBO.setTimeoutNum(showHeartbeatBO.getTimeoutNum() / cycleNum);
            showHeartbeatBO.setDowngradeNum(showHeartbeatBO.getDowngradeNum() / cycleNum);
        }

        result.setHeartbeatList(showList);

        return result;
    }

    /**
     * 合并数据
     *
     * @param mainHeartbeatBO
     * @param addHeartbeatBO
     */
    private void mergeHeartbeatBO(HeartbeatBO mainHeartbeatBO, HeartbeatBO addHeartbeatBO) {
        mainHeartbeatBO.setVisitNum(mainHeartbeatBO.getVisitNum() + addHeartbeatBO.getVisitNum());
        mainHeartbeatBO.setMaxConcurrentNum(Math.max(mainHeartbeatBO.getMaxConcurrentNum(),
                addHeartbeatBO.getMaxConcurrentNum()));
        mainHeartbeatBO.setExceptionNum(mainHeartbeatBO.getExceptionNum() + addHeartbeatBO.getExceptionNum());
        mainHeartbeatBO.setTimeoutNum(mainHeartbeatBO.getTimeoutNum() + addHeartbeatBO.getTimeoutNum());
        mainHeartbeatBO.setDowngradeNum(mainHeartbeatBO.getDowngradeNum() + addHeartbeatBO.getDowngradeNum());
    }

    /**
     * 根据开始时间和结束时间计算页面上展示的数据点粒度
     *
     * @param startTime
     * @param endTime
     * @return 每多少毫秒展示成一个点，最大粒度1小时一个点，最小粒度为10秒钟一个点
     */
    private Long calShowTimeCycle(Date startTime, Date endTime) {
        long interval = endTime.getTime() - startTime.getTime();

        /**
         * 两小时内每10秒展示成一个点，这是最小粒度
         */
        if (interval <= TWO_HOUR) {
            return 10000L;

        } else if (interval > TWO_HOUR && interval <= ONE_DAY) {
            return 120000L;

        } else if (interval > ONE_DAY && interval <= TWO_DAY) {
            return 240000L;

        } else {
            return 600000L;
        }
    }

    /**
     * 保存心跳数据
     *
     * @param heartBeatRequest
     */
    private void saveRecord(HeartbeatRequest heartBeatRequest) {

        Map<String, SdsCycleInfo> map = heartBeatRequest.getPointInfoMap();
        if (map.isEmpty()) {
            return;
        }

        String appGroupName = heartBeatRequest.getAppGroupName();
        String appName = heartBeatRequest.getAppName();
        String ip = heartBeatRequest.getIp();
        String hostname = heartBeatRequest.getHostname();
        Date statisticsCycleTime = heartBeatRequest.getStatisticsCycleTime();

        List<HeartbeatDO> dataList = Lists.newArrayList();
        List<PointDictDO> pointDictDOList = Lists.newArrayList();
        for (Map.Entry<String, SdsCycleInfo> entry : map.entrySet()) {
            SdsCycleInfo sdsCycleInfo = entry.getValue();

            // 为了节省空间，访问量为0的数据不存储
            if (sdsCycleInfo.getVisitNum() == null || sdsCycleInfo.getVisitNum() == 0) {
                continue;
            }

            HeartbeatDO heartbeatDO = new HeartbeatDO();
            heartbeatDO.setAppGroupName(appGroupName);
            heartbeatDO.setAppName(appName);
            heartbeatDO.setPoint(sdsCycleInfo.getPoint());
            heartbeatDO.setDowngradeNum(sdsCycleInfo.getDowngradeNum());
            heartbeatDO.setVisitNum(sdsCycleInfo.getVisitNum());
            heartbeatDO.setExceptionNum(sdsCycleInfo.getExceptionNum());
            heartbeatDO.setTimeoutNum(sdsCycleInfo.getTimeoutNum());
            heartbeatDO.setMaxConcurrentNum(sdsCycleInfo.getConcurrentNum());
            heartbeatDO.setAppIp(ip);
            heartbeatDO.setStatisticsCycleTime(statisticsCycleTime);

            dataList.add(heartbeatDO);

            //构建降级点list
            buildPointDictDOList(pointDictDOList, sdsCycleInfo, appGroupName, appName);
        }

        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }

        try {
            //保存到降级点字典表
            pointDictService.addPointList(pointDictDOList);

            // 保存到数据库
            heartbeatDao.addHeartbeat(dataList);

            //检查心跳降级点与库中降级点是否一致,一些废弃的降级点需要删除
            pointDictService.checkAndDeleteDeadPoint(map, appGroupName, appName);
        } catch (Exception e) {
            logger.error("插入心跳数据异常，dataList:" + JSON.toJSONString(dataList), e);
        }
    }

    /**
     * 构建新增降级点list
     *
     * @param pointDictDOList
     * @param sdsCycleInfo
     * @param appGroupName
     * @param appName
     */
    private void buildPointDictDOList(List<PointDictDO> pointDictDOList, SdsCycleInfo sdsCycleInfo,
                                      String appGroupName, String appName) {
        PointDictDO pointDictDO = new PointDictDO();
        pointDictDO.setAppGroupName(appGroupName);
        pointDictDO.setAppName(appName);
        pointDictDO.setPoint(sdsCycleInfo.getPoint());
        pointDictDOList.add(pointDictDO);
    }
}
