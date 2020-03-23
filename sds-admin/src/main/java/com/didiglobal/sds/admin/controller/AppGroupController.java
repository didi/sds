package com.didiglobal.sds.admin.controller;


import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.admin.controller.request.AppGroupRequest;
import com.didiglobal.sds.admin.controller.response.SdsResponse;
import com.didiglobal.sds.admin.dao.AppGroupDao;
import com.didiglobal.sds.admin.dao.AppInfoDao;
import com.didiglobal.sds.admin.dao.bean.AppGroupDO;
import com.didiglobal.sds.admin.dao.bean.AppInfoDO;
import com.didiglobal.sds.admin.util.StringCheck;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.didiglobal.sds.admin.constants.SdsCode.*;

/**
 * 应用组控制器
 * <p>
 * Created by manzhizhen on 18/1/7.
 */
@RestController
@RequestMapping(value = "/sds/appgroup/", method = RequestMethod.POST)
public class AppGroupController {

    @Autowired
    private AppGroupDao appGroupDao;
    @Autowired
    private AppInfoDao appInfoDao;

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    @RequestMapping(value = "listall")
    public SdsResponse<List<String>> queryAllAppGroup() {
        List<AppGroupDO> appGroupDOList = appGroupDao.queryAllAppGroup();

        if (CollectionUtils.isEmpty(appGroupDOList)) {
            return new SdsResponse<>(Lists.newArrayList());
        }

        return new SdsResponse<>(appGroupDOList.stream().map(AppGroupDO::getAppGroupName).collect(Collectors.toList()));
    }

    // curl -i -v -X POST -H 'Content-type':'application/json' -H 'Origin':'http://10.90.23.30:8000'
    // -H 'Referer':' http://10.90.23.30:8000/applicationGroup' -H 'Access-Control-Request-Headers':'content-type'
    // -H 'Access-Control-Request-Method':'POST' -d '{"appGroupName":"BikeBusinessDepartment"}'
    // http://10.88.129.19:8000/sds/appgroup/listpage
    //
    // curl -X POST -H 'Content-type':'application/json'  -d '{"appGroupName":"BikeBusinessDepartment"}'
    // http://localhost:8887/sds/appgroup/listpage
    @RequestMapping(value = "listpage")
    public SdsResponse<List<AppGroupDO>> queryAppGroupByPage(@RequestBody AppGroupRequest appGroupRequest) {

        if (appGroupRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        String appGroupName = appGroupRequest.getAppGroupName();

        Integer page = appGroupRequest.getPage();
        Integer pageSize = appGroupRequest.getPageSize();

        page = page == null ? 1 : page;
        pageSize = pageSize == null ? 10 : pageSize;

        List<AppGroupDO> appGroupDOList = appGroupDao.queryAppGroupByPage(appGroupName, (page - 1) * pageSize,
                pageSize);

        return new SdsResponse<>(appGroupDOList);
    }

    // curl -X POST -H 'Content-type':'application/json'  -d '{"appGroupName":"BikeBusinessDepartment", "operatorId":1}'
    // http://localhost:8887/sds/appgroup/add
    @RequestMapping(value = "add")
    public SdsResponse addAppGroup(@RequestBody AppGroupRequest appGroupRequest) {

        if (appGroupRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(appGroupRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

//        if (StringUtils.isBlank(appGroupRequest.getCreatorName())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "创建者名称不能为空");
//        }
//
//        if (StringUtils.isBlank(appGroupRequest.getCreatorEmail())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "创建者邮箱不能为空");
//        }

        if (!StringCheck.checkStringName(appGroupRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称只能是数字、字母和-");
        }

        if (appGroupRequest.getAppGroupName().length() > 30) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能超过30个字符");
        }

        AppGroupDO appGroupDO = appGroupDao.queryByGroupName(appGroupRequest.getAppGroupName());
        if (appGroupDO != null) {
            return new SdsResponse(PARAM_ERROR.getCode(), appGroupRequest.getAppGroupName() + "已经存在！");
        }

        int result = appGroupDao.addAppGroup(new AppGroupDO(appGroupRequest.getAppGroupName(), appGroupRequest.
                getCreatorName(), appGroupRequest.getCreatorEmail()));
        return result == 1 ? new SdsResponse(SUCCESS.getCode(), "新增应用组成功") : new SdsResponse(SYSTEM_ERROR.
                getCode(), "新增应用组失败");
    }

    // curl -X POST -H 'Content-type':'application/json'
    // -d '{"appGroupName":"黑马无敌", "newAppGroupName":"黑马狂奔", "operatorId":4}'
    // http://localhost:8887/sds/appgroup/edit
    @RequestMapping(value = "edit")
    public SdsResponse updateAppGroup(@RequestBody AppGroupRequest appGroupRequest) {

        if (appGroupRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(appGroupRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(appGroupRequest.getNewAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "新应用组名称不能为空");
        }

        if (Objects.equals(appGroupRequest.getAppGroupName(), appGroupRequest.getNewAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "新老名字不能相同");
        }

//        if (StringUtils.isBlank(appGroupRequest.getOperatorName())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "操作人姓名不能为空");
//        }
//
//        if (StringUtils.isBlank(appGroupRequest.getOperatorEmail())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "操作人邮箱不能为空");
//        }

        if (!StringCheck.checkStringName(appGroupRequest.getNewAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "新应用组名称只能是数字、字母和-");
        }

        if (appGroupRequest.getNewAppGroupName().length() > 30) {
            return new SdsResponse(PARAM_ERROR.getCode(), "新应用组名称不能超过30个字符");
        }

        AppGroupDO appGroupDO = appGroupDao.queryByGroupName(appGroupRequest.getAppGroupName());
        if (appGroupDO == null) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), "修改应用组失败，" + appGroupRequest.getAppGroupName() +
                    "该应用组不存在");
        }

        AppGroupDO oldAppGroupDO = appGroupDao.queryByGroupName(appGroupRequest.getNewAppGroupName());
        if (oldAppGroupDO != null) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), "修改AppGroup失败，" + appGroupRequest.
                    getNewAppGroupName() + "该AppGroup已存在");
        }

        List<AppInfoDO> appInfoDOS = appInfoDao.queryAppInfoByAppGroup(appGroupRequest.getAppGroupName());
        if (CollectionUtils.isNotEmpty(appInfoDOS)) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), "修改应用组失败，" + appGroupRequest.getNewAppGroupName()
                    + "该应用组已存在");

        }

        int result = appGroupDao.updateAppGroup(appGroupRequest.getAppGroupName(), appGroupRequest.getNewAppGroupName(),
                appGroupRequest.getOperatorName(),
                appGroupRequest.getOperatorEmail());
        return result == 1 ? new SdsResponse(SUCCESS.getCode(), "修改应用组成功") : new SdsResponse(SYSTEM_ERROR.
                getCode(), "修改应用组失败");
    }

    // curl -X POST -H 'Content-type':'application/json'  -d '{"appGroupName":"黑马狂奔"}'
    // http://localhost:8887/sds/appgroup/delete
    @RequestMapping(value = "delete")
    public SdsResponse deleteAppGroup(@RequestBody AppGroupRequest appGroupRequest) {

        if (appGroupRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(appGroupRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        List<AppInfoDO> appInfoDOS = appInfoDao.queryAppInfoByAppGroup(appGroupRequest.getAppGroupName());
        if (CollectionUtils.isNotEmpty(appInfoDOS)) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), "删除应用组失败，该应用组下还有App！");
        }

        int result = appGroupDao.deleteAppGroup(appGroupRequest.getAppGroupName());
        return result == 1 ? new SdsResponse(SUCCESS.getCode(), appGroupRequest.getAppGroupName() +
                "删除应用组成功") : new SdsResponse(SYSTEM_ERROR.getCode(),
                appGroupRequest.getAppGroupName() + "删除应用组失败");
    }

    @ExceptionHandler
    public void handler(Exception e) {
        logger.error("AppGroupController has exception.", e);
    }

}
