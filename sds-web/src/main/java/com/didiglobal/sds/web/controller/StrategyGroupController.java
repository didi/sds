package com.didiglobal.sds.web.controller;


import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.web.controller.request.StrategyGroupRequest;
import com.didiglobal.sds.web.controller.response.SdsResponse;
import com.didiglobal.sds.web.dao.AppGroupDao;
import com.didiglobal.sds.web.dao.AppInfoDao;
import com.didiglobal.sds.web.dao.PointStrategyDao;
import com.didiglobal.sds.web.dao.StrategyGroupDao;
import com.didiglobal.sds.web.dao.bean.AppGroupDO;
import com.didiglobal.sds.web.dao.bean.AppInfoDO;
import com.didiglobal.sds.web.dao.bean.PointStrategyDO;
import com.didiglobal.sds.web.dao.bean.StrategyGroupDO;
import com.didiglobal.sds.web.util.StringCheck;
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

import static com.didiglobal.sds.web.constants.SdsCode.*;

/**
 * 策略组控制器
 * <p>
 * Created by yizhenqiang on 18/1/7.
 */
@RestController
@RequestMapping(value = "/sds/strategygroup/", method = RequestMethod.POST)
public class StrategyGroupController {

    @Autowired
    private AppGroupDao appGroupDao;
    @Autowired
    private AppInfoDao appInfoDao;
    @Autowired
    private StrategyGroupDao strategyGroupDao;
    @Autowired
    private PointStrategyDao pointStrategyDao;

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    @RequestMapping(value = "listall")
    public SdsResponse<List<String>> queryAllStrategyGroup(@RequestBody StrategyGroupRequest strategyGroupRequest) {

        if (strategyGroupRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        List<StrategyGroupDO> strategyGroupList =
                strategyGroupDao.queryAllStrategyGroup(strategyGroupRequest.getAppGroupName(),
                strategyGroupRequest.getAppName());

        List<String> strategyGroupNameList = Lists.newArrayList();
        for (StrategyGroupDO strategyGroupDO : strategyGroupList) {
            strategyGroupNameList.add(strategyGroupDO.getStrategyGroupName());
        }

        return new SdsResponse<>(strategyGroupNameList);
    }

    // curl -X POST -H 'Content-type':'application/json'  -d '{"appGroupName":"黑马"}'
    // http://localhost:8887/sds/strategygroup/listpage
    @RequestMapping(value = "listpage")
    public SdsResponse<List<StrategyGroupDO>> queryStrategyGroupByPage(@RequestBody StrategyGroupRequest
                                                                               strategyGroupRequest) {

        if (strategyGroupRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请选择一个应用组");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请选择一个应用");
        }

        Integer page = strategyGroupRequest.getPage();
        Integer pageSize = strategyGroupRequest.getPageSize();

        page = page == null ? 1 : page;
        pageSize = pageSize == null ? 10 : pageSize;

        List<StrategyGroupDO> strategyGroupList = strategyGroupDao.queryStrategyGroupByPage(strategyGroupRequest.
                        getAppGroupName(),
                strategyGroupRequest.getAppName(), strategyGroupRequest.getStrategyGroupName(),
                (page - 1) * pageSize, pageSize);

        return new SdsResponse(strategyGroupList);
    }

    // curl -X POST -H 'Content-type':'application/json'  -d '{"appGroupName":"黑马", "appName":"mzz-study",
    // "strategyGroupName":"FIRST_GROUP", "operatorId":2}'  http://localhost:8887/sds/strategygroup/add
    @RequestMapping(value = "add")
    public SdsResponse addStrategyGroup(@RequestBody StrategyGroupRequest strategyGroupRequest) {

        if (strategyGroupRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getStrategyGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "策略组名称不能为空");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getCreatorName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "创建者姓名不能为空");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getCreatorEmail())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "创建者邮箱不能为空");
        }

        if (!StringCheck.checkChineseStringName(strategyGroupRequest.getStrategyGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "策略组名称只能是汉字、数字、字母和-");
        }

        if (strategyGroupRequest.getStrategyGroupName().length() > 30) {
            return new SdsResponse(PARAM_ERROR.getCode(), "策略组名称不能超过30个字符");
        }

        AppGroupDO appGroupDO = appGroupDao.queryByGroupName(strategyGroupRequest.getAppGroupName());
        if (appGroupDO == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), strategyGroupRequest.getAppGroupName() + "不存在！");
        }

        AppInfoDO appInfo = appInfoDao.queryAppInfo(strategyGroupRequest.getAppGroupName(), strategyGroupRequest.
                getAppName());
        if (appInfo == null) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), strategyGroupRequest.getAppName() + "不存在！");
        }

        StrategyGroupDO strategyGroupDO = strategyGroupDao.queryByGroupName(strategyGroupRequest.getAppGroupName(),
                strategyGroupRequest.getAppName(),
                strategyGroupRequest.getStrategyGroupName());
        if (strategyGroupDO != null) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), strategyGroupRequest.getStrategyGroupName() +
                    "已经存在！");
        }

        int result = strategyGroupDao.addStrategyGroup(new StrategyGroupDO(strategyGroupRequest.getAppGroupName(),
                strategyGroupRequest.getAppName(), strategyGroupRequest.getStrategyGroupName(),
                strategyGroupRequest.getCreatorName(), strategyGroupRequest.getCreatorEmail()));

        return result == 1 ? new SdsResponse(SUCCESS.getCode(), "新增策略组成功") :
                new SdsResponse(SYSTEM_ERROR.getCode(), "新增策略组失败");
    }

    // curl -X POST -H 'Content-type':'application/json'  -d '{"appGroupName":"黑马无敌", "newAppGroupName":"黑马狂奔",
    // "operatorId":4}'  http://localhost:8887/sds/strategygroup/edit
    @RequestMapping(value = "edit")
    public SdsResponse updateStrategyGroup(@RequestBody StrategyGroupRequest strategyGroupRequest) {

        if (strategyGroupRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getStrategyGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "策略组名称不能为空");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getNewStrategyGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "新策略组名称不能为空");
        }

        if (Objects.equals(strategyGroupRequest.getStrategyGroupName(), strategyGroupRequest.
                getNewStrategyGroupName())) {
            return new SdsResponse(SUCCESS.getCode(), "策略组名称没有变化，不需要更新");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getOperatorName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "操作者姓名不能为空");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getOperatorEmail())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "操作者邮箱不能为空");
        }

        if (strategyGroupRequest.getNewStrategyGroupName().length() > 30) {
            return new SdsResponse(PARAM_ERROR.getCode(), "新策略组名称不能超过30个字符");
        }

        StrategyGroupDO strategyGroupDO = strategyGroupDao.queryByGroupName(strategyGroupRequest.getAppGroupName(),
                strategyGroupRequest.getAppName(),
                strategyGroupRequest.getNewStrategyGroupName());
        if (strategyGroupDO != null) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), strategyGroupRequest.getStrategyGroupName() +
                    "已经存在！");
        }

        AppInfoDO appInfoDO = appInfoDao.queryAppInfo(strategyGroupRequest.getAppGroupName(), strategyGroupRequest.
                getAppName());
        if (appInfoDO == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用不存在");
        }
        if (Objects.equals(appInfoDO.getStrategyGroupName(), strategyGroupRequest.getStrategyGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "该策略组正被当前应用使用，无法修改！");
        }

        int hasPointStrategyCount = pointStrategyDao.queryPointStrategyCountByStrategyGroup(strategyGroupRequest.
                        getAppGroupName(), strategyGroupRequest.getAppName(),
                strategyGroupRequest.getStrategyGroupName());
        if (hasPointStrategyCount > 0) {
            return new SdsResponse(PARAM_ERROR.getCode(), "该策略组下面配置了" + hasPointStrategyCount +
                    "个降级点策略，无法修改！");
        }

        int result = strategyGroupDao.updateStrategyGroup(strategyGroupRequest.getAppGroupName(),
                strategyGroupRequest.getAppName(), strategyGroupRequest.getStrategyGroupName(),
                strategyGroupRequest.getNewStrategyGroupName(), strategyGroupRequest.getOperatorName(),
                strategyGroupRequest.getOperatorEmail());
        return result == 1 ? new SdsResponse(SUCCESS.getCode(), "修改策略组成功") :
                new SdsResponse(SYSTEM_ERROR.getCode(), "修改策略组失败");
    }

    @RequestMapping(value = "clone")
    public SdsResponse cloneStrategyGroup(@RequestBody StrategyGroupRequest strategyGroupRequest) {

        if (strategyGroupRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getStrategyGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "策略组名称不能为空");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getNewStrategyGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "新策略组名称不能为空");
        }

        if (Objects.equals(strategyGroupRequest.getStrategyGroupName(),
                strategyGroupRequest.getNewStrategyGroupName())) {
            new SdsResponse(SUCCESS.getCode(), "策略组名称没有变化，不需要克隆");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getCreatorName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "创建者名称不能为空");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getCreatorEmail())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "创建者邮箱不能为空");
        }

        StrategyGroupDO sourceStrategyGroupDO =
                strategyGroupDao.queryByGroupName(strategyGroupRequest.getAppGroupName(),
                        strategyGroupRequest.getAppName(),
                strategyGroupRequest.getStrategyGroupName());
        if (sourceStrategyGroupDO == null) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), strategyGroupRequest.getStrategyGroupName() + "不存在！");
        }

        StrategyGroupDO newStrategyGroupDO = strategyGroupDao.queryByGroupName(strategyGroupRequest.getAppGroupName()
                , strategyGroupRequest.getAppName(),
                strategyGroupRequest.getNewStrategyGroupName());
        if (newStrategyGroupDO != null) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), strategyGroupRequest.getNewStrategyGroupName() + "已存在！");
        }

        int result = strategyGroupDao.addStrategyGroup(new StrategyGroupDO(strategyGroupRequest.getAppGroupName(),
                strategyGroupRequest.getAppName(),
                strategyGroupRequest.getNewStrategyGroupName(), strategyGroupRequest.getCreatorName(),
                strategyGroupRequest.getCreatorEmail()));
        if (result != 1) {
            new SdsResponse(SYSTEM_ERROR.getCode(), "新增策略组失败，克隆策略组没有完成");
        }

        List<PointStrategyDO> pointStrategyList =
                pointStrategyDao.queryPointStrategyByStrategyGroup(strategyGroupRequest.getAppGroupName(),
                        strategyGroupRequest.getAppName(),
                null, strategyGroupRequest.getNewStrategyGroupName());
        if (CollectionUtils.isNotEmpty(pointStrategyList)) {

            List<PointStrategyDO> clonePointStrategyList = pointStrategyList.stream().map(pointStrategyDO -> {

                pointStrategyDO.setStrategyGroupName(strategyGroupRequest.getNewStrategyGroupName());
                pointStrategyDO.setCreatorName(strategyGroupRequest.getCreatorName());
                pointStrategyDO.setCreatorEmail(strategyGroupRequest.getCreatorEmail());
                pointStrategyDO.setOperatorName(strategyGroupRequest.getCreatorName());
                pointStrategyDO.setOperatorEmail(strategyGroupRequest.getCreatorEmail());

                return pointStrategyDO;

            }).collect(Collectors.toList());

            result = pointStrategyDao.addPointStrategyBatch(clonePointStrategyList);
            if (result != clonePointStrategyList.size()) {
                new SdsResponse(SYSTEM_ERROR.getCode(), "新增降级点策略失败，克隆策略组没有完成");
            }
        }

        return new SdsResponse(SUCCESS.getCode(), "克隆策略组成功");
    }

    // curl -X POST -H 'Content-type':'application/json'  -d '{"appGroupName":"黑马狂奔"}'
    // http://localhost:8887/sds/strategygroup/delete
    @RequestMapping(value = "delete")
    public SdsResponse deleteStrategyGroup(@RequestBody StrategyGroupRequest strategyGroupRequest) {

        if (strategyGroupRequest == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getAppGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getAppName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getStrategyGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "策略组名称不能为空");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getOperatorName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "操作者姓名不能为空");
        }

        if (StringUtils.isBlank(strategyGroupRequest.getOperatorEmail())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "操作者邮箱不能为空");
        }

        AppInfoDO appInfoDO = appInfoDao.queryAppInfo(strategyGroupRequest.getAppGroupName(),
                strategyGroupRequest.getAppName());
        if (appInfoDO == null) {
            return new SdsResponse(PARAM_ERROR.getCode(), "删除策略组失败，应用不存在");
        }
        if (Objects.equals(appInfoDO.getStrategyGroupName(), strategyGroupRequest.getStrategyGroupName())) {
            return new SdsResponse(PARAM_ERROR.getCode(), "该策略组正被当前应用使用，无法删除！");
        }

        List<PointStrategyDO> pointStrategyList =
                pointStrategyDao.queryPointStrategyByStrategyGroup(strategyGroupRequest.getAppGroupName(),
                strategyGroupRequest.getAppName(), null, strategyGroupRequest.getStrategyGroupName());
        if (CollectionUtils.isNotEmpty(pointStrategyList)) {
            return new SdsResponse(SYSTEM_ERROR.getCode(), "删除策略组失败，该策略组下还有降级点策略！");
        }

        int result = strategyGroupDao.deleteStrategyGroup(strategyGroupRequest.getAppGroupName(),
                strategyGroupRequest.getAppName(), strategyGroupRequest.getStrategyGroupName());
        return result == 1 ? new SdsResponse(SUCCESS.getCode(), strategyGroupRequest.getStrategyGroupName() +
                "删除策略组成功") : new SdsResponse(SYSTEM_ERROR.getCode(),
                strategyGroupRequest.getStrategyGroupName() + "删除策略组失败");
    }

    @ExceptionHandler
    public void handler(Exception e) {
        logger.error("StrategyGroupController has exception.", e);
    }

}
