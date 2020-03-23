package com.didiglobal.sds.admin.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.client.util.StringUtils;
import com.didiglobal.sds.admin.dao.HeartbeatDao;
import com.didiglobal.sds.admin.dao.bean.HeartbeatDO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 当使用influxdb来保存心跳数据时，初始化该类
 * @Author: manzhizhen
 * @Date: Create in 2019-09-15 00:02
 */
@ConditionalOnProperty(name = "sds.heartbeat.database.type", havingValue = "influxdb")
@ConfigurationProperties(prefix = "sds.influxdb") // 有set方法才生效？
@Repository
public class InfluxHeartbeatDaoImpl implements HeartbeatDao {

    @Autowired
    private RestTemplate restTemplate;

    private String url;
    private String database;
    private String username;
    private String password;

    private String writeUrl;
    private String queryUrl;

    private String line = System.lineSeparator();

    private final static String INFLUXDB_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    @PostConstruct
    public void init() {
        // 补充前缀和后缀
        url = url.startsWith("http://") ? url : url + "http://";
        url = url.endsWith("/") ? url : url + "/";

        writeUrl = url + "write?db=" + database;
        queryUrl = url + "query?db=" + database;
    }

    @Override
    public int addHeartbeat(List<HeartbeatDO> heartbeatDOList) {
        if (CollectionUtils.isEmpty(heartbeatDOList)) {
            return 0;
        }

        /**
         * 构造插入influxdb的语句
         */
        String insertStr = buildInsertString(heartbeatDOList);

        ResponseEntity<String> exchange = restTemplate.exchange(writeUrl, HttpMethod.POST,
                new HttpEntity<String>(insertStr), String.class);

        if (exchange.getStatusCode().is2xxSuccessful()) {
            return heartbeatDOList.size();
        }

        return 0;
    }

    @Override
    public List<HeartbeatDO> queryHeartbeatList(String appGroupName, String appName, String point, Date startTime,
                                                Date endTime) {

        List<HeartbeatDO> result = Lists.newArrayList();

        if (StringUtils.isBlank(appGroupName) || StringUtils.isBlank(appName) || StringUtils.isBlank(point) || startTime == null || endTime == null) {
            return null;
        }

        /**
         * 构建influxdb的查询字符串
         */
        String queryStr = buildQueryString(appGroupName, appName, point, startTime, endTime);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("q", queryStr);
        ResponseEntity<String> exchange = restTemplate.exchange(queryUrl, HttpMethod.POST, new HttpEntity<>(params,
                null), String.class);

        if (!exchange.getStatusCode().is2xxSuccessful() || StringUtils.isBlank(exchange.getBody())) {
            return null;
        }

//        {
//            "results": [{
//                "statement_id": 0,
//                "series": [{
//                    "name": "heartbeat",
//                    "columns": ["time", "appGroupName", "appName", "concurrentNum", "downgradeNum", "exceptionNum",
//                    "ip", "point", "timeoutNum", "visitNum"],
//                    "values": [
//                            ["2019-09-15T08:45:00.844Z", "htw", "htw-order", 15, 10, 99, "10.2.2.9", "visitPoint",
//                            23, 100000],
//                            ["2019-09-15T08:45:21.959Z", "htw", "htw-order", 15, 10, 99, "10.2.2.9", "visitPoint",
//                            23, 100000]
//                        ]
//                    }
//                ]
//            }]
//        }

        /**
         * todo 这块代码后续需要优化 manzhizhen
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(INFLUXDB_DATE_FORMAT);
        JSONObject responseJsonObject = JSONObject.parseObject(exchange.getBody());
        JSONArray responseResultsJsonArray = responseJsonObject.getJSONArray("results");
        JSONObject resultJsonObject = (JSONObject) responseResultsJsonArray.get(0);
        JSONArray seriesJsonArray = resultJsonObject.getJSONArray("series");
        JSONObject heartbeatJsonObject = (JSONObject) seriesJsonArray.get(0);
        JSONArray columnsJsonArray = heartbeatJsonObject.getJSONArray("columns");
        JSONArray valuesJsonArray = heartbeatJsonObject.getJSONArray("values");

        List<Object> conlumsList = columnsJsonArray.toJavaList(Object.class);
        List<Object> valuesList = valuesJsonArray.toJavaList(Object.class);
        Map<String, Object> map = Maps.newHashMap();
        for (int i = 0; i < valuesList.size(); i++) {
            for (int j = 0; j < conlumsList.size(); j++) {
                map.put(conlumsList.get(j).toString(), ((List) valuesList.get(i)).get(j));
            }

            HeartbeatDO heartbeatDO = new HeartbeatDO();
            try {
                heartbeatDO.setAppGroupName(map.get("appGroupName").toString());
                heartbeatDO.setAppName(map.get("appName").toString());
                heartbeatDO.setPoint(map.get("point").toString());
                heartbeatDO.setDowngradeNum(Long.valueOf(map.get("downgradeNum").toString()));
                heartbeatDO.setVisitNum(Long.valueOf(map.get("visitNum").toString()));
                heartbeatDO.setExceptionNum(Long.valueOf(map.get("exceptionNum").toString()));
                heartbeatDO.setTimeoutNum(Long.valueOf(map.get("timeoutNum").toString()));
                heartbeatDO.setMaxConcurrentNum(Integer.valueOf(map.get("concurrentNum").toString()));
                heartbeatDO.setStatisticsCycleTime(simpleDateFormat.parse(map.get("time").toString()));

                result.add(heartbeatDO);

            } catch (Exception e) {
                logger.warn("InfluxHeartbeatDaoImpl#queryHeartbeatList 心跳数据转换异常：" + JSON.toJSONString(map), e);
            }
        }

        return result;
    }

    /**
     * 构建influxdb的查询字符串
     * <p>
     * influxdb查询字符串举例：curl -G 'http://localhost:8086/query?pretty=true&db=sds' --data-urlencode "q=SELECT * from
     * heartbeat where time > '2015-08-18T06:06:00.331Z'"
     *
     * @param appGroupName
     * @param appName
     * @param point
     * @param startTime
     * @param endTime
     * @return
     */
    private String buildQueryString(String appGroupName, String appName, String point, Date startTime, Date endTime) {
        StringBuilder queryStr = new StringBuilder("select * from heartbeat where ");

        queryStr.append(" appGroupName = ").append("'").append(appGroupName).append("' ");
        queryStr.append(" and appName = ").append("'").append(appName).append("' ");
        queryStr.append(" and point = ").append("'").append(point).append("' ");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(INFLUXDB_DATE_FORMAT);
        queryStr.append(" and time >= '").append(simpleDateFormat.format(startTime)).append("' ");
        queryStr.append(" and time <= ").append("'").append(simpleDateFormat.format(endTime)).append("' ");

        return queryStr.toString();
    }

    /**
     * 构建influxdb的插入字符串
     * <p>
     * influxdb http插入数据举例： curl -i -XPOST 'http://localhost:8086/write?db=sds' --data-binary 'heartbeat,
     * appGroupName=hm,appName=hm-order,point=queryOrderInfoPoint,ip=10.0.1.3 visitNum=1000,exceptionNum=10,
     * concurrentNum=10,timeoutNum=15,downgradeNum=25 1568475381686000000'
     *
     * @param heartbeatDOList
     * @return
     */
    private String buildInsertString(List<HeartbeatDO> heartbeatDOList) {
        StringBuilder insertStr = new StringBuilder();

        heartbeatDOList.stream().forEach(heartbeatDO -> {
            // 添加measurement
            insertStr.append("heartbeat,");

            // 添加tag
            insertStr.append("appGroupName=").append(heartbeatDO.getAppGroupName()).append(",")
                    .append("appName=").append(heartbeatDO.getAppName()).append(",")
                    .append("point=").append(heartbeatDO.getPoint()).append(",")
                    .append("ip=").append(heartbeatDO.getAppIp()).append(" ");

            // 添加kv
            insertStr.append("visitNum=").append(heartbeatDO.getVisitNum()).append(",")
                    .append("exceptionNum=").append(heartbeatDO.getExceptionNum()).append(",")
                    .append("concurrentNum=").append(heartbeatDO.getMaxConcurrentNum()).append(",")
                    .append("timeoutNum=").append(heartbeatDO.getTimeoutNum()).append(",")
                    .append("downgradeNum=").append(heartbeatDO.getDowngradeNum()).append(" ");

            // 添加timestamp，注意，单位是纳秒
            insertStr.append(heartbeatDO.getStatisticsCycleTime().getTime() * 1000000);

            // 为了一次性能加多条数据，这里加一个换行符
            insertStr.append(System.lineSeparator());

        });

        return insertStr.toString();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
