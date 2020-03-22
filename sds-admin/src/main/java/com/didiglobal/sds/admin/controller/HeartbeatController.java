/**
 *
 */
package com.didiglobal.sds.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.didiglobal.sds.client.bean.HeartBeatResponse;
import com.didiglobal.sds.client.bean.HeartbeatRequest;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.admin.controller.request.HeartbeatQueryRequest;
import com.didiglobal.sds.admin.controller.response.SdsResponse;
import com.didiglobal.sds.admin.service.HeartbeatService;
import com.didiglobal.sds.admin.service.bean.HeartbeatShowBO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.didiglobal.sds.admin.constants.SdsCode.PARAM_ERROR;

/**
 * 心跳页面控制器
 *
 * @author manzhizhen
 */
@RestController
@RequestMapping(value = "/sds/heartbeat/", method = RequestMethod.POST)
public class HeartbeatController {
    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    @Autowired
    private HeartbeatService heartbeatService;

    @RequestMapping(value = "add")
    public HeartBeatResponse heartbeat(HttpServletRequest request) {
        HeartbeatRequest heartbeatRequest = null;
        try {
            heartbeatRequest = JSONObject.parseObject(request.getParameter("client"), HeartbeatRequest.class);
        } catch (Exception e) {
            logger.warn("HeartbeatController#heartbeat json转HeartBeatRequest异常", e);
        }

        if (!checkClientRequest(heartbeatRequest)) {
            logger.warn("HeartbeatController#heartbeat 客户端请求非法：" + request.getParameter("client"));
            HeartBeatResponse hbResponse = new HeartBeatResponse();
            hbResponse.setErrorMsg("客户端请求非法：" + request.getParameter("client"));

            return hbResponse;
        }

        return heartbeatService.saveHeartbeatInfo(heartbeatRequest);
    }

    @RequestMapping(value = "pullstrategy")
    public HeartBeatResponse pullPointStrategy(HttpServletRequest request) {
        HeartbeatRequest heartbeatRequest = null;
        try {
            heartbeatRequest = JSONObject.parseObject(request.getParameter("client"), HeartbeatRequest.class);
        } catch (Exception e) {
            logger.warn("HeartbeatController#pullPointStrategy json转HeartBeatRequest异常", e);
        }

        if (!checkClientRequest(heartbeatRequest)) {
            logger.warn("HeartbeatController#pullPointStrategy 客户端请求非法：" + request.getParameter("client"));
            HeartBeatResponse hbResponse = new HeartBeatResponse();
            hbResponse.setErrorMsg("客户端请求非法：" + request.getParameter("client"));

            return hbResponse;
        }

        return heartbeatService.checkAndGetNewestPointStrategy(heartbeatRequest);
    }

    /**
     * (曲线图)查询心跳信息
     *
     * @return
     */
    @RequestMapping("listpage")
    public SdsResponse queryHeartbeatList(@RequestBody HeartbeatQueryRequest heartbeatQueryRequest) {

        if (heartbeatQueryRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "heartbeatQueryRequest");
        }

        if (StringUtils.isBlank(heartbeatQueryRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "appGroupName不能为空");
        }

        if (StringUtils.isBlank(heartbeatQueryRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "appName不能为空");
        }

        if (StringUtils.isBlank(heartbeatQueryRequest.getPoint())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "point不能为空");
        }

        HeartbeatShowBO heartbeatShowBO = heartbeatService.queryHeartbeatList(heartbeatQueryRequest.getAppGroupName(),
                heartbeatQueryRequest.getAppName(), heartbeatQueryRequest.getPoint(),
                heartbeatQueryRequest.getStartTime(), heartbeatQueryRequest.getEndTime());

        return new SdsResponse(heartbeatShowBO);

    }

    /**
     * 检查心跳数据是否有效
     *
     * @param request
     * @return
     */
    private boolean checkClientRequest(HeartbeatRequest request) {
        if (request == null || StringUtils.isBlank(request.getAppGroupName()) ||
                StringUtils.isBlank(request.getAppName()) || StringUtils.isBlank(request.getIp())) {
            return false;
        }
        return true;
    }
}
