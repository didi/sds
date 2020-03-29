package com.didiglobal.sds.admin.controller;


import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.client.util.StringUtils;
import com.didiglobal.sds.admin.controller.request.PointReturnValueRequest;
import com.didiglobal.sds.admin.controller.response.SdsResponse;
import com.didiglobal.sds.admin.dao.AppInfoDao;
import com.didiglobal.sds.admin.dao.PointReturnValueDao;
import com.didiglobal.sds.admin.dao.bean.PointReturnValueDO;
import com.didiglobal.sds.admin.util.FastBeanUtil;
import com.didiglobal.sds.admin.util.StringCheck;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.didiglobal.sds.admin.constants.SdsCode.PARAM_ERROR;
import static com.didiglobal.sds.admin.constants.SdsCode.SUCCESS;

/**
 * 降级点返回值控制器
 * <p>
 * Created by manzhizhen on 18/1/7.
 */
@RestController
@RequestMapping(value = "/sds/pointreturnvalue/", method = RequestMethod.POST)
public class PointReturnValueController {

    @Autowired
    private PointReturnValueDao pointReturnValueDao;
    @Autowired
    private AppInfoDao appInfoDao;

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    @RequestMapping(value = "listpage")
    public SdsResponse<List<PointReturnValueDO>> queryPointReturnValueByPage(@RequestBody PointReturnValueRequest pointReturnValueRequest) {

        if (pointReturnValueRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        Integer page = pointReturnValueRequest.getPage();
        Integer pageSize = pointReturnValueRequest.getPageSize();

        page = page == null ? 1 : page;
        pageSize = pageSize == null ? 10 : pageSize;

        List<PointReturnValueDO> data = pointReturnValueDao.queryPointReturnValueByPage(pointReturnValueRequest.
                        getAppGroupName(), pointReturnValueRequest.getAppName(), pointReturnValueRequest.getPoint(),
                pointReturnValueRequest.getReturnValueStr(), (page - 1) * pageSize, pageSize);

        return new SdsResponse<>(data);
    }

    // curl -X POST -H 'Content-type':'application/json'  -d '{"appGroupName":"BikeBusinessDepartment", "appName":"order",
    // "sdsSchemeName":"FIRST_GROUP", "point":"love", "status":1, "operatorId":999 }'
    // http://localhost:8887/sds/pointstrategy/add
    @RequestMapping(value = "add")
    public SdsResponse addPointReturnValue(@RequestBody PointReturnValueRequest pointReturnValueRequest) {

        if (pointReturnValueRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(pointReturnValueRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(pointReturnValueRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        if (StringUtils.isBlank(pointReturnValueRequest.getPoint())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "降级点不能为空");
        }

        if (pointReturnValueRequest.getStatus() == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "状态不能为空");
        }

//        if (StringUtils.isBlank(pointReturnValueRequest.getCreatorName())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "创建者姓名不能为空");
//        }
//
//        if (StringUtils.isBlank(pointReturnValueRequest.getCreatorEmail())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "创建者邮箱不能为空");
//        }

        if (!StringCheck.checkStringName(pointReturnValueRequest.getPoint())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "降级点名称只能是数字、字母、-、_和#");
        }

        if (pointReturnValueRequest.getPoint().length() > 200) {
            return new SdsResponse(PARAM_ERROR.getCode(), "降级点名称不能超过200个字符");
        }


        PointReturnValueDO pointReturnValueDO = pointReturnValueDao.queryByPoint(pointReturnValueRequest.
                getAppGroupName(), pointReturnValueRequest.getAppName(), pointReturnValueRequest.getPoint());
        if (pointReturnValueDO != null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "降级点" + pointReturnValueDO.getPoint() + "已存在！");
        }

        pointReturnValueRequest.setOperatorName(pointReturnValueRequest.getCreatorName());
        pointReturnValueRequest.setOperatorEmail(pointReturnValueRequest.getCreatorEmail());
        int result = pointReturnValueDao.addPointReturnValue(FastBeanUtil.copyForNew(pointReturnValueRequest,
                new PointReturnValueDO()));

        if (result == 1) {
            appInfoDao.increaseAppVersion(pointReturnValueRequest.getAppGroupName(), pointReturnValueRequest.
                            getAppName(), pointReturnValueRequest.getCreatorName(),
                    pointReturnValueRequest.getCreatorEmail());
        }

        return result == 1 ? new SdsResponse(SUCCESS.getCode(), "新增降级点返回值成功") :
                new SdsResponse(507, "新增降级点返回值失败");
    }

    @RequestMapping(value = "edit")
    public SdsResponse updatePointReturnValue(@RequestBody PointReturnValueRequest pointReturnValueRequest) {

        if (pointReturnValueRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(pointReturnValueRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(pointReturnValueRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        if (StringUtils.isBlank(pointReturnValueRequest.getPoint())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "降级点不能为空");
        }

        if (pointReturnValueRequest.getStatus() == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "状态不能为空");
        }

//        if (StringUtils.isBlank(pointReturnValueRequest.getOperatorName())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "操作者姓名不能为空");
//        }
//
//        if (StringUtils.isBlank(pointReturnValueRequest.getOperatorEmail())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "操作者邮箱不能为空");
//        }

        if (pointReturnValueRequest.getPoint().length() > 200) {
            return new SdsResponse(PARAM_ERROR.getCode(), "降级点名称不能超过200个字符");
        }

        PointReturnValueDO pointReturnValueDO = pointReturnValueDao.queryByPoint(pointReturnValueRequest.
                getAppGroupName(), pointReturnValueRequest.getAppName(), pointReturnValueRequest.getPoint());
        if (pointReturnValueDO == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "降级点返回值" + pointReturnValueRequest.getPoint() +
                    "不存在！");
        }

        int result = pointReturnValueDao.updatePointReturnValue(pointReturnValueRequest.getAppGroupName(),
                pointReturnValueRequest.getAppName(), pointReturnValueRequest.getPoint(),
                pointReturnValueRequest.getReturnValueStr(), pointReturnValueRequest.getStatus(),
                pointReturnValueRequest.getOperatorName(), pointReturnValueRequest.getOperatorEmail());

        if (result == 1) {
            appInfoDao.increaseAppVersion(pointReturnValueRequest.getAppGroupName(), pointReturnValueRequest.
                            getAppName(), pointReturnValueRequest.getOperatorName(),
                    pointReturnValueRequest.getOperatorEmail());
        }

        return result == 1 ? new SdsResponse(SUCCESS.getCode(), "修改降级点返回值成功") : new SdsResponse(508,
                "修改降级点返回值失败");
    }

    @RequestMapping(value = "delete")
    public SdsResponse deletePointReturnValue(@RequestBody PointReturnValueRequest pointReturnValueRequest) {

        if (pointReturnValueRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        int result = pointReturnValueDao.deletePointReturnValue(pointReturnValueRequest.getAppGroupName(),
                pointReturnValueRequest.getAppName(), pointReturnValueRequest.getPoint());

        if (result == 1) {
            appInfoDao.increaseAppVersion(pointReturnValueRequest.getAppGroupName(), pointReturnValueRequest.
                            getAppName(), pointReturnValueRequest.getOperatorName(),
                    pointReturnValueRequest.getOperatorEmail());
        }

        return result == 1 ? new SdsResponse(SUCCESS.getCode(), "删除降级点返回值成功") : new SdsResponse(509,
                "删除降级点返回值失败");
    }

//    @ExceptionHandler
//    public void handler(Exception e) {
//        logger.error("PointReturnValueController has exception.", e);
//    }
}
