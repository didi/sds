package com.didiglobal.sds.admin.controller;


import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.admin.controller.request.SdsSchemeRequest;
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
 * 降级预案控制器
 * <p>
 * Created by manzhizhen on 18/1/7.
 */
@RestController
@RequestMapping(value = "/sds/sdsscheme/", method = RequestMethod.POST)
public class SdsSchemeController {

    @Autowired
    private AppGroupDao appGroupDao;
    @Autowired
    private AppInfoDao appInfoDao;
    @Autowired
    private SdsSchemeDao sdsSchemeDao;
    @Autowired
    private PointStrategyDao pointStrategyDao;

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    @RequestMapping(value = "listall")
    public SdsResponse<List<String>> queryAllSdsScheme(@RequestBody SdsSchemeRequest sdsSchemeRequest) {

        if (sdsSchemeRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        List<SdsSchemeDO> sdsSchemeList =
                sdsSchemeDao.queryAllSdsScheme(sdsSchemeRequest.getAppGroupName(),
                sdsSchemeRequest.getAppName());

        List<String> sdsSchemeNameList = Lists.newArrayList();
        for (SdsSchemeDO sdsSchemeDO : sdsSchemeList) {
            sdsSchemeNameList.add(sdsSchemeDO.getSdsSchemeName());
        }

        return new SdsResponse<>(sdsSchemeNameList);
    }

    // curl -X POST -H 'Content-type':'application/json'  -d '{"appGroupName":"BikeBusinessDepartment"}'
    // http://localhost:8887/sds/sdsscheme/listpage
    @RequestMapping(value = "listpage")
    public SdsResponse<List<SdsSchemeDO>> querySdsSchemeByPage(@RequestBody SdsSchemeRequest
                                                                               sdsSchemeRequest) {

        if (sdsSchemeRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请选择一个应用组");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请选择一个应用");
        }

        Integer page = sdsSchemeRequest.getPage();
        Integer pageSize = sdsSchemeRequest.getPageSize();

        page = page == null ? 1 : page;
        pageSize = pageSize == null ? 10 : pageSize;

        List<SdsSchemeDO> sdsSchemeList = sdsSchemeDao.querySdsSchemeByPage(sdsSchemeRequest.
                        getAppGroupName(),
                sdsSchemeRequest.getAppName(), sdsSchemeRequest.getSdsSchemeName(),
                (page - 1) * pageSize, pageSize);

        return new SdsResponse(sdsSchemeList);
    }

    // curl -X POST -H 'Content-type':'application/json'  -d '{"appGroupName":"BikeBusinessDepartment", "appName":"order",
    // "sdsSchemeName":"FIRST_GROUP", "operatorId":2}'  http://localhost:8887/sds/sdsscheme/add
    @RequestMapping(value = "add")
    public SdsResponse addSdsScheme(@RequestBody SdsSchemeRequest sdsSchemeRequest) {

        if (sdsSchemeRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getSdsSchemeName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "降级预案名称不能为空");
        }

//        if (StringUtils.isBlank(sdsSchemeRequest.getCreatorName())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "创建者姓名不能为空");
//        }
//
//        if (StringUtils.isBlank(sdsSchemeRequest.getCreatorEmail())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "创建者邮箱不能为空");
//        }

        if (!StringCheck.checkChineseStringName(sdsSchemeRequest.getSdsSchemeName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "降级预案名称只能是汉字、数字、字母和-");
        }

        if (sdsSchemeRequest.getSdsSchemeName().length() > 30) {
            return new SdsResponse(PARAM_ERROR.getCode(), "降级预案名称不能超过30个字符");
        }

        AppGroupDO appGroupDO = appGroupDao.queryByGroupName(sdsSchemeRequest.getAppGroupName());
        if (appGroupDO == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), sdsSchemeRequest.getAppGroupName() + "不存在！");
        }

        AppInfoDO appInfo = appInfoDao.queryAppInfo(sdsSchemeRequest.getAppGroupName(), sdsSchemeRequest.
                getAppName());
        if (appInfo == null) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), sdsSchemeRequest.getAppName() + "不存在！");
        }

        SdsSchemeDO sdsSchemeDO = sdsSchemeDao.queryByGroupName(sdsSchemeRequest.getAppGroupName(),
                sdsSchemeRequest.getAppName(),
                sdsSchemeRequest.getSdsSchemeName());
        if (sdsSchemeDO != null) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), sdsSchemeRequest.getSdsSchemeName() +
                    "已经存在！");
        }

        int result = sdsSchemeDao.addSdsScheme(new SdsSchemeDO(sdsSchemeRequest.getAppGroupName(),
                sdsSchemeRequest.getAppName(), sdsSchemeRequest.getSdsSchemeName(),
                sdsSchemeRequest.getCreatorName(), sdsSchemeRequest.getCreatorEmail()));

        return result == 1 ? new SdsResponse(SUCCESS.getCode(), "新增降级预案成功") :
                new SdsResponse(SYSTEM_ERROR.getCode(), "新增降级预案失败");
    }

    // curl -X POST -H 'Content-type':'application/json'  -d '{"appGroupName":"黑马无敌", "newAppGroupName":"黑马狂奔",
    // "operatorId":4}'  http://localhost:8887/sds/sdsscheme/edit
    @RequestMapping(value = "edit")
    public SdsResponse updateSdsScheme(@RequestBody SdsSchemeRequest sdsSchemeRequest) {

        if (sdsSchemeRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getSdsSchemeName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "降级预案名称不能为空");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getNewSdsSchemeName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "新降级预案名称不能为空");
        }

        if (Objects.equals(sdsSchemeRequest.getSdsSchemeName(), sdsSchemeRequest.
                getNewSdsSchemeName())) {
            return new SdsResponse(SUCCESS.getCode(), "降级预案名称没有变化，不需要更新");
        }

//        if (StringUtils.isBlank(sdsSchemeRequest.getOperatorName())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "操作者姓名不能为空");
//        }
//
//        if (StringUtils.isBlank(sdsSchemeRequest.getOperatorEmail())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "操作者邮箱不能为空");
//        }

        if (sdsSchemeRequest.getNewSdsSchemeName().length() > 30) {
            return new SdsResponse(PARAM_ERROR.getCode(), "新降级预案名称不能超过30个字符");
        }

        SdsSchemeDO sdsSchemeDO = sdsSchemeDao.queryByGroupName(sdsSchemeRequest.getAppGroupName(),
                sdsSchemeRequest.getAppName(),
                sdsSchemeRequest.getNewSdsSchemeName());
        if (sdsSchemeDO != null) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), sdsSchemeRequest.getSdsSchemeName() +
                    "已经存在！");
        }

        AppInfoDO appInfoDO = appInfoDao.queryAppInfo(sdsSchemeRequest.getAppGroupName(), sdsSchemeRequest.
                getAppName());
        if (appInfoDO == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用不存在");
        }
        if (Objects.equals(appInfoDO.getSdsSchemeName(), sdsSchemeRequest.getSdsSchemeName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "该降级预案正被当前应用使用，无法修改！");
        }

        int hasPointStrategyCount = pointStrategyDao.queryPointStrategyCountBySdsScheme(sdsSchemeRequest.
                        getAppGroupName(), sdsSchemeRequest.getAppName(),
                sdsSchemeRequest.getSdsSchemeName());
        if (hasPointStrategyCount > 0) {
            return new SdsResponse(PARAM_ERROR.getCode(), "该降级预案下面配置了" + hasPointStrategyCount +
                    "个降级点策略，无法修改！");
        }

        int result = sdsSchemeDao.updateSdsScheme(sdsSchemeRequest.getAppGroupName(),
                sdsSchemeRequest.getAppName(), sdsSchemeRequest.getSdsSchemeName(),
                sdsSchemeRequest.getNewSdsSchemeName(), sdsSchemeRequest.getOperatorName(),
                sdsSchemeRequest.getOperatorEmail());
        return result == 1 ? new SdsResponse(SUCCESS.getCode(), "修改降级预案成功") :
                new SdsResponse(SYSTEM_ERROR.getCode(), "修改降级预案失败");
    }

    @RequestMapping(value = "clone")
    public SdsResponse cloneSdsScheme(@RequestBody SdsSchemeRequest sdsSchemeRequest) {

        if (sdsSchemeRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getSdsSchemeName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "降级预案名称不能为空");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getNewSdsSchemeName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "新降级预案名称不能为空");
        }

        if (Objects.equals(sdsSchemeRequest.getSdsSchemeName(),
                sdsSchemeRequest.getNewSdsSchemeName())) {
            new SdsResponse(SUCCESS.getCode(), "降级预案名称没有变化，不需要克隆");
        }

//        if (StringUtils.isBlank(sdsSchemeRequest.getCreatorName())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "创建者名称不能为空");
//        }
//
//        if (StringUtils.isBlank(sdsSchemeRequest.getCreatorEmail())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "创建者邮箱不能为空");
//        }

        SdsSchemeDO sourceSdsSchemeDO =
                sdsSchemeDao.queryByGroupName(sdsSchemeRequest.getAppGroupName(),
                        sdsSchemeRequest.getAppName(),
                sdsSchemeRequest.getSdsSchemeName());
        if (sourceSdsSchemeDO == null) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), sdsSchemeRequest.getSdsSchemeName() + "不存在！");
        }

        SdsSchemeDO newSdsSchemeDO = sdsSchemeDao.queryByGroupName(sdsSchemeRequest.getAppGroupName()
                , sdsSchemeRequest.getAppName(),
                sdsSchemeRequest.getNewSdsSchemeName());
        if (newSdsSchemeDO != null) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), sdsSchemeRequest.getNewSdsSchemeName() + "已存在！");
        }

        int result = sdsSchemeDao.addSdsScheme(new SdsSchemeDO(sdsSchemeRequest.getAppGroupName(),
                sdsSchemeRequest.getAppName(),
                sdsSchemeRequest.getNewSdsSchemeName(), sdsSchemeRequest.getCreatorName(),
                sdsSchemeRequest.getCreatorEmail()));
        if (result != 1) {
            new SdsResponse(SYSTEM_ERROR.getCode(), "新增降级预案失败，克隆降级预案没有完成");
        }

        List<PointStrategyDO> pointStrategyList =
                pointStrategyDao.queryPointStrategyBySdsScheme(sdsSchemeRequest.getAppGroupName(),
                        sdsSchemeRequest.getAppName(),
                null, sdsSchemeRequest.getNewSdsSchemeName());
        if (CollectionUtils.isNotEmpty(pointStrategyList)) {

            List<PointStrategyDO> clonePointStrategyList = pointStrategyList.stream().map(pointStrategyDO -> {

                pointStrategyDO.setSdsSchemeName(sdsSchemeRequest.getNewSdsSchemeName());
                pointStrategyDO.setCreatorName(sdsSchemeRequest.getCreatorName());
                pointStrategyDO.setCreatorEmail(sdsSchemeRequest.getCreatorEmail());
                pointStrategyDO.setOperatorName(sdsSchemeRequest.getCreatorName());
                pointStrategyDO.setOperatorEmail(sdsSchemeRequest.getCreatorEmail());

                return pointStrategyDO;

            }).collect(Collectors.toList());

            result = pointStrategyDao.addPointStrategyBatch(clonePointStrategyList);
            if (result != clonePointStrategyList.size()) {
                new SdsResponse(SYSTEM_ERROR.getCode(), "新增降级点策略失败，克隆降级预案没有完成");
            }
        }

        return new SdsResponse(SUCCESS.getCode(), "克隆降级预案成功");
    }

    // curl -X POST -H 'Content-type':'application/json'  -d '{"appGroupName":"黑马狂奔"}'
    // http://localhost:8887/sds/sdsscheme/delete
    @RequestMapping(value = "delete")
    public SdsResponse deleteSdsScheme(@RequestBody SdsSchemeRequest sdsSchemeRequest) {

        if (sdsSchemeRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        if (StringUtils.isBlank(sdsSchemeRequest.getSdsSchemeName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "降级预案名称不能为空");
        }

//        if (StringUtils.isBlank(sdsSchemeRequest.getOperatorName())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "操作者姓名不能为空");
//        }
//
//        if (StringUtils.isBlank(sdsSchemeRequest.getOperatorEmail())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "操作者邮箱不能为空");
//        }

        AppInfoDO appInfoDO = appInfoDao.queryAppInfo(sdsSchemeRequest.getAppGroupName(),
                sdsSchemeRequest.getAppName());
        if (appInfoDO == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "删除降级预案失败，应用不存在");
        }
        if (Objects.equals(appInfoDO.getSdsSchemeName(), sdsSchemeRequest.getSdsSchemeName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "该降级预案正被当前应用使用，无法删除！");
        }

        List<PointStrategyDO> pointStrategyList =
                pointStrategyDao.queryPointStrategyBySdsScheme(sdsSchemeRequest.getAppGroupName(),
                sdsSchemeRequest.getAppName(), null, sdsSchemeRequest.getSdsSchemeName());
        if (CollectionUtils.isNotEmpty(pointStrategyList)) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), "删除降级预案失败，该降级预案下还有降级点策略！");
        }

        int result = sdsSchemeDao.deleteSdsScheme(sdsSchemeRequest.getAppGroupName(),
                sdsSchemeRequest.getAppName(), sdsSchemeRequest.getSdsSchemeName());
        return result == 1 ? new SdsResponse(SUCCESS.getCode(), sdsSchemeRequest.getSdsSchemeName() +
                "删除降级预案成功") : new SdsResponse(SYSTEM_ERROR.getCode(),
                sdsSchemeRequest.getSdsSchemeName() + "删除降级预案失败");
    }

    @ExceptionHandler
    public void handler(Exception e) {
        logger.error("SdsSchemeController has exception.", e);
    }

}
