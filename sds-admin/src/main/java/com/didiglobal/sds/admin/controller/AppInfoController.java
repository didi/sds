package com.didiglobal.sds.admin.controller;

import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.admin.controller.request.AppInfoRequest;
import com.didiglobal.sds.admin.controller.response.SdsResponse;
import com.didiglobal.sds.admin.dao.AppGroupDao;
import com.didiglobal.sds.admin.dao.AppInfoDao;
import com.didiglobal.sds.admin.dao.PointStrategyDao;
import com.didiglobal.sds.admin.dao.SdsSchemeDao;
import com.didiglobal.sds.admin.dao.bean.AppGroupDO;
import com.didiglobal.sds.admin.dao.bean.AppInfoDO;
import com.didiglobal.sds.admin.dao.bean.PointStrategyDO;
import com.didiglobal.sds.admin.dao.bean.SdsSchemeDO;
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
 * 应用信息控制器
 * <p>
 * Created by manzhizhen on 18/1/7.
 */
@RestController
@RequestMapping(value = "/sds/appinfo/", method = RequestMethod.POST)
public class AppInfoController {

    @Autowired
    private AppInfoDao appInfoDao;
    @Autowired
    private AppGroupDao appGroupDao;
    @Autowired
    private SdsSchemeDao sdsSchemeDao;
    @Autowired
    private PointStrategyDao pointStrategyDao;

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    // curl -X POST -H 'Content-type':'application/json'  -d '{"appGroupName":"BikeBusinessDepartment"}'
    // http://localhost:8887/sds/appinfo/listpage
    @RequestMapping(value = "listpage")
    public SdsResponse<List<AppInfoDO>> queryAppInfoByPage(@RequestBody AppInfoRequest appInfoRequest) {

        if (appInfoRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        Integer page = appInfoRequest.getPage();
        Integer pageSize = appInfoRequest.getPageSize();

        page = page == null ? 1 : page;
        pageSize = pageSize == null ? 10 : pageSize;

        List<AppInfoDO> appInfoDOList = appInfoDao.queryAppInfoByPage(appInfoRequest.getAppGroupName(),
                appInfoRequest.getAppName(), (page - 1) * pageSize, pageSize);

        return new SdsResponse<>(appInfoDOList);
    }

    @RequestMapping(value = "listall")
    public SdsResponse<List<String>> queryAppListByAppGroup(@RequestBody AppInfoRequest appInfoRequest) {

        if (appInfoRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(appInfoRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "appGroupName不能为空");
        }

        List<AppInfoDO> appInfoDOList = appInfoDao.queryAppInfoByAppGroup(appInfoRequest.getAppGroupName());
        if (CollectionUtils.isNotEmpty(appInfoDOList)) {
            return new SdsResponse<>(appInfoDOList.stream().map(AppInfoDO::getAppName).collect(Collectors.toList()));
        }

        return new SdsResponse<>(Lists.newArrayList());
    }

    // curl -X POST -H 'Content-type':'application/json'  -d '{"appGroupName":"BikeBusinessDepartment", "appName":"bh-order",
    // "operatorId":2}'  http://localhost:8887/sds/appinfo/add
    @RequestMapping(value = "add")
    public SdsResponse addAppInfo(@RequestBody AppInfoRequest appInfoRequest) {
        if (appInfoRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(appInfoRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(appInfoRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能为空");
        }

//        if (StringUtils.isBlank(appInfoRequest.getCreatorName())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "创建者姓名不能为空");
//        }
//
//        if (StringUtils.isBlank(appInfoRequest.getCreatorEmail())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "创建者邮箱不能为空");
//        }

        if (!StringCheck.checkStringName(appInfoRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称只能是数字、字母和-");
        }

        if (appInfoRequest.getAppName().length() > 30) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能超过30个字符");
        }

        AppGroupDO appGroupDO = appGroupDao.queryByGroupName(appInfoRequest.getAppGroupName());
        if (appGroupDO == null) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), "新增应用失败，应用组不存在");
        }

        AppInfoDO oldAppInfo = appInfoDao.queryAppInfo(appInfoRequest.getAppGroupName(), appInfoRequest.getAppName());
        if (oldAppInfo != null) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), "新增应用失败，同名的已经存在");
        }

        AppInfoDO appInfoDO = new AppInfoDO();
        appInfoDO.setAppGroupName(appInfoRequest.getAppGroupName());
        appInfoDO.setAppName(appInfoRequest.getAppName());
        appInfoDO.setSdsSchemeName(appInfoRequest.getSdsSchemeName());
        appInfoDO.setCreatorName(appInfoRequest.getCreatorName());
        appInfoDO.setCreatorEmail(appInfoRequest.getCreatorEmail());
        appInfoDO.setOperatorName(appInfoRequest.getCreatorName());
        appInfoDO.setOperatorEmail(appInfoRequest.getCreatorEmail());

        int result = appInfoDao.addAppInfo(appInfoDO);

        if (result == 1) {
            /**
             *  如果添加应用成功，那么立马给该应用创建一个默认降级预案，并将该默认降级预案设置为该应用的当前降级预案
             */
            SdsSchemeDO sdsSchemeDO = new SdsSchemeDO();
            sdsSchemeDO.setAppGroupName(appInfoRequest.getAppGroupName());
            sdsSchemeDO.setAppName(appInfoRequest.getAppName());

            sdsSchemeDO.setSdsSchemeName("默认降级预案");
            sdsSchemeDO.setCreatorName(appInfoRequest.getCreatorName());
            sdsSchemeDO.setCreatorEmail(appInfoRequest.getCreatorEmail());
            sdsSchemeDO.setOperatorName(appInfoRequest.getCreatorName());
            sdsSchemeDO.setOperatorEmail(appInfoRequest.getCreatorEmail());

            result = sdsSchemeDao.addSdsScheme(sdsSchemeDO);

            if (result == 1) {
                // 将该默认降级预案设置成该应用的当前降级预案
                appInfoDao.updateAppInfo(appInfoRequest.getAppGroupName(), appInfoRequest.getAppName(), appInfoRequest.
                                getAppName(), sdsSchemeDO.getSdsSchemeName(),
                        appInfoRequest.getCreatorName(), appInfoRequest.getCreatorEmail());
            }
        }

        return result == 1 ? new SdsResponse(SUCCESS.getCode(), "新增应用成功") :
                new SdsResponse(SYSTEM_ERROR.getCode(), "新增应用失败");
    }

    // curl -X POST -H 'Content-type':'application/json'  -d '{"appGroupName":"BikeBusinessDepartment", "appName":"bh-order",
    // "newAppName":"bh-ins", "newSdsSchemeName":"abc", "operatorId":2}'  http://localhost:8887/sds/appinfo/edit
    @RequestMapping(value = "edit")
    public SdsResponse<List<AppInfoDO>> updateAppInfo(@RequestBody AppInfoRequest appInfoRequest) {

        if (appInfoRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(appInfoRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(appInfoRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        if (StringUtils.isBlank(appInfoRequest.getNewAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "新应用名称不能为空");
        }

        if (!StringCheck.checkStringName(appInfoRequest.getNewAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "新应用名称只能是数字、字母和-");
        }

        if (appInfoRequest.getNewAppName().length() > 30) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能超过30个字符");
        }

        boolean modifyAppName = false;
        if (!Objects.equals(appInfoRequest.getAppName(), appInfoRequest.getNewAppName())) {
            AppInfoDO appInfoDO = appInfoDao.queryAppInfo(appInfoRequest.getAppGroupName(), appInfoRequest.
                    getNewAppName());
            if (appInfoDO != null) {
                return new SdsResponse(PARAM_ERROR.getCode(), appInfoRequest.getNewAppName() + "应用名称已存在");
            }

            modifyAppName = true;
        }

        /**
         * 如果要修改应用名称，那么如果应用下面配置了降级点策略，那么不允许修改，因为很可能这时候已经有线上的应用使用了该应用名称了，
         * 服务端修改后将导致这些线上应用上传心跳和拉取配置失败
         * 所以，只有该应用下没有配置降级点策略才允许修改应用名字
         */
        if (modifyAppName) {
            List<PointStrategyDO> pointStrategyList = pointStrategyDao.queryPointStrategyBySdsScheme(appInfoRequest.
                            getAppGroupName(),
                    appInfoRequest.getAppName(), null, null);
            if (CollectionUtils.isNotEmpty(pointStrategyList)) {
                return new SdsResponse(PARAM_ERROR.getCode(), appInfoRequest.getAppName() + "应用下配置了降级点策略，" +
                        "不能修改应用名称！");
            }
        }

        if (StringUtils.isNotBlank(appInfoRequest.getNewSdsSchemeName())) {
            SdsSchemeDO sdsSchemeDO = sdsSchemeDao.queryByGroupName(appInfoRequest.getAppGroupName(),
                    appInfoRequest.getAppName(),
                    appInfoRequest.getNewSdsSchemeName());
            if (sdsSchemeDO == null) {
                return new SdsResponse(PARAM_ERROR.getCode(), appInfoRequest.getNewSdsSchemeName() +
                        "降级预案不存在");
            }
        }

        int result = appInfoDao.updateAppInfo(appInfoRequest.getAppGroupName(), appInfoRequest.getAppName(),
                appInfoRequest.getNewAppName(), appInfoRequest.getNewSdsSchemeName(), appInfoRequest.
                        getOperatorName(), appInfoRequest.getOperatorEmail());

        if (result == 1) {
            sdsSchemeDao.updateSdsSchemeAppNameBatch(appInfoRequest.getAppGroupName(),
                    appInfoRequest.getAppName(), appInfoRequest.getNewAppName(), appInfoRequest.getOperatorName(),
                    appInfoRequest.getOperatorEmail());
        }

        return result == 1 ? new SdsResponse(SUCCESS.getCode(), "修改应用成功") :
                new SdsResponse(SYSTEM_ERROR.getCode(), "修改应用失败");
    }

    @RequestMapping(value = "delete")
    public SdsResponse<List<AppInfoDO>> deleteAppInfo(@RequestBody AppInfoRequest appInfoRequest) {

        if (appInfoRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(appInfoRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(appInfoRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        List<PointStrategyDO> pointStrategyList = pointStrategyDao.queryPointStrategyBySdsScheme(
                appInfoRequest.getAppGroupName(), appInfoRequest.getAppName(), null, null);
        if (CollectionUtils.isNotEmpty(pointStrategyList)) {
            return new SdsResponse(PARAM_ERROR.getCode(), appInfoRequest.getAppName() + "应用下配置了降级点策略，" +
                    "不能删除该应用！");
        }

        int result = appInfoDao.deleteAppInfo(appInfoRequest.getAppGroupName(), appInfoRequest.getAppName());
        return result == 1 ? new SdsResponse(SUCCESS.getCode(), "删除应用成功") :
                new SdsResponse(SYSTEM_ERROR.getCode(), "删除应用失败");
    }

    @ExceptionHandler
    public void handler(Exception e) {
        logger.error("AppInfoController has exception.", e);
    }

}
