package com.didiglobal.sds.admin.controller;

import com.didiglobal.sds.client.util.StringUtils;
import com.didiglobal.sds.admin.controller.bean.PointDictVO;
import com.didiglobal.sds.admin.controller.request.DashboardPointRequest;
import com.didiglobal.sds.admin.controller.request.DashboardRequest;
import com.didiglobal.sds.admin.controller.response.DashboardResponse;
import com.didiglobal.sds.admin.controller.response.SdsResponse;
import com.didiglobal.sds.admin.dao.bean.PointDictDO;
import com.didiglobal.sds.admin.service.DashboardService;
import com.didiglobal.sds.admin.service.PointDictService;
import com.didiglobal.sds.admin.service.bean.DashboardMainBO;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.didiglobal.sds.admin.constants.SdsCode.PARAM_ERROR;

/**
 * @Description: 包含访问量、并发量、错误量、超时量和降级量的仪表盘控制器
 * @Author: manzhizhen
 * @Date: Create in 2019-09-11 10:07
 */
@RestController
@RequestMapping(value = "/sds/dashboard/", method = RequestMethod.POST)
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private PointDictService pointDictService;

    @RequestMapping(value = "main")
    public SdsResponse<DashboardResponse> queryPointReturnValueByPage(@RequestBody DashboardRequest dashboardRequest) {

        if (dashboardRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(dashboardRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(dashboardRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        if (StringUtils.isBlank(dashboardRequest.getPoint())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "降级点不能为空");
        }

        if (dashboardRequest.getStartTime() == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "开始时间不能为空");
        }

        if (dashboardRequest.getEndTime() == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "结束时间不能为空");
        }

        DashboardMainBO dashboardMainBO = dashboardService.queryDashboardMainShowInfo(dashboardRequest.
                        getAppGroupName(), dashboardRequest.getAppName(), dashboardRequest.getPoint(),
                dashboardRequest.getStartTime(), dashboardRequest.getEndTime());

        DashboardResponse response = new DashboardResponse();
        response.setAppGroupName(dashboardRequest.getAppGroupName());
        response.setAppName(dashboardRequest.getAppName());
        response.setPoint(dashboardRequest.getPoint());

        if (dashboardMainBO != null) {
            response.setVisitList(dashboardMainBO.getVisitList());
            response.setConcurrentList(dashboardMainBO.getConcurrentList());
            response.setExceptionList(dashboardMainBO.getExceptionList());
            response.setTimeoutList(dashboardMainBO.getTimeoutList());
            response.setDowngradeList(dashboardMainBO.getDowngradeList());
        }

        return new SdsResponse<>(response);
    }

    @RequestMapping(value = "listpage")
    public SdsResponse<List<PointDictVO>> queryPointByPage(@RequestBody DashboardPointRequest dashboardPointRequest) {
        if (dashboardPointRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        List<PointDictVO> result = Lists.newArrayList();
        List<PointDictDO> pointDictDOS = pointDictService.queryPointDictListByPage(dashboardPointRequest.getAppGroupName(), dashboardPointRequest.getAppName(),
                dashboardPointRequest.getPoint(), dashboardPointRequest.getPage(), dashboardPointRequest.getPageSize());

        if (!CollectionUtils.isEmpty(pointDictDOS)) {
            result = pointDictDOS.stream().map(pointDictDO -> {
                PointDictVO pointDictVO = new PointDictVO();
                pointDictVO.setAppGroupName(pointDictDO.getAppGroupName());
                pointDictVO.setAppName(pointDictDO.getAppName());
                pointDictVO.setPoint(pointDictDO.getPoint());
                return pointDictVO;

            }).collect(Collectors.toList());
        }

        return new SdsResponse<>(result);
    }
}
