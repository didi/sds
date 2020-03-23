package com.didiglobal.sds.admin.controller;


import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.client.util.StringUtils;
import com.didiglobal.sds.admin.constants.SdsCode;
import com.didiglobal.sds.admin.controller.request.PointStrategyRequest;
import com.didiglobal.sds.admin.controller.response.SdsResponse;
import com.didiglobal.sds.admin.dao.AppInfoDao;
import com.didiglobal.sds.admin.dao.PointStrategyDao;
import com.didiglobal.sds.admin.dao.SdsSchemeDao;
import com.didiglobal.sds.admin.dao.bean.AppInfoDO;
import com.didiglobal.sds.admin.dao.bean.PointStrategyDO;
import com.didiglobal.sds.admin.dao.bean.SdsSchemeDO;
import com.didiglobal.sds.admin.util.FastBeanUtil;
import com.didiglobal.sds.admin.util.StringCheck;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * 降级点策略控制器
 * <p>
 * Created by manzhizhen on 18/1/7.
 */
@RestController
@RequestMapping(value = "/sds/pointstrategy/", method = RequestMethod.POST)
public class PointStrategyController {

    @Autowired
    private PointStrategyDao pointStrategyDao;
    @Autowired
    private SdsSchemeDao sdsSchemeDao;
    @Autowired
    private AppInfoDao appInfoDao;

    private final static String STRATEGY_TEMPLATE = "提示：应用组：%s，应用：%s 当前所使用的降级预案是【%s】（注意，不属于该降级预案的降级点策略将对该应用不生效）";

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    @RequestMapping(value = "listpage")
    public SdsResponse<List<PointStrategyDO>> queryPointStrategyByPage(@RequestBody PointStrategyRequest
                                                                               pointStrategyRequest) {

        if (pointStrategyRequest == null) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

//        if (StringUtils.isBlank(pointStrategyRequest.getAppGroupName())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "请选择一个应用组");
//        }
//
//        if (StringUtils.isBlank(pointStrategyRequest.getAppName())) {
//            return new SdsResponse(PARAM_ERROR.getCode(), "请选择一个应用");
//        }

        Integer page = pointStrategyRequest.getPage();
        Integer pageSize = pointStrategyRequest.getPageSize();

        page = page == null ? 1 : page;
        pageSize = pageSize == null ? 10 : pageSize;

        List<PointStrategyDO> data = pointStrategyDao.queryPointStrategyByPage(pointStrategyRequest.getAppGroupName(),
                pointStrategyRequest.getAppName(), pointStrategyRequest.getPoint(), pointStrategyRequest.
                        getSdsSchemeName(), (page - 1) * pageSize, pageSize);

        return new SdsResponse<>(data);
    }

    @RequestMapping(value = "querysdsschemetips")
    public SdsResponse<String> queryAppCurSdsSchemeTips(@RequestBody PointStrategyRequest pointStrategyRequest) {

        if (pointStrategyRequest == null) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(pointStrategyRequest.getAppGroupName())) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "请选择一个应用组");
        }

        if (StringUtils.isBlank(pointStrategyRequest.getAppName())) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "请选择一个应用");
        }

        AppInfoDO appInfoDO = appInfoDao.queryAppInfo(pointStrategyRequest.getAppGroupName(),
                pointStrategyRequest.getAppName());

        if (appInfoDO == null) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "应用不存在，应用组：" +
                    pointStrategyRequest.getAppGroupName() + "，应用：" + pointStrategyRequest.getAppName());
        }

        String tips = String.format(STRATEGY_TEMPLATE, appInfoDO.getAppGroupName(), appInfoDO.getAppName(),
                appInfoDO.getSdsSchemeName());

        return new SdsResponse<>(tips);
    }

    // curl -X POST -H 'Content-type':'application/json'  -d '{"appGroupName":"BikeBusinessDepartment", "appName":"order",
    // "sdsSchemeName":"FIRST_GROUP", "point":"love", "status":1, "operatorId":999 }'
    // http://localhost:8887/sds/pointstrategy/add
    @RequestMapping(value = "add")
    public SdsResponse addPointStrategy(@RequestBody PointStrategyRequest pointStrategyRequest) {

        if (pointStrategyRequest == null) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(pointStrategyRequest.getAppGroupName())) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(pointStrategyRequest.getAppName())) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        if (StringUtils.isBlank(pointStrategyRequest.getSdsSchemeName())) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "降级预案名称不能为空");
        }

        if (StringUtils.isBlank(pointStrategyRequest.getPoint())) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "降级点不能为空");
        }

        if (pointStrategyRequest.getDowngradeRate() == null) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "降级比例不能为空");
        }

        if (pointStrategyRequest.getStatus() == null) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "状态不能为空");
        }

//        if (StringUtils.isBlank(pointStrategyRequest.getCreatorName())) {
//            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "创建者姓名不能为空");
//        }
//
//        if (StringUtils.isBlank(pointStrategyRequest.getCreatorEmail())) {
//            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "创建者邮箱不能为空");
//        }

        if (!StringCheck.checkStringName(pointStrategyRequest.getPoint())) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "降级点名称只能是数字、字母和-");
        }

        if (pointStrategyRequest.getPoint().length() > 200) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "降级点名称不能超过200个字符");
        }

        SdsSchemeDO sdsSchemeDO = sdsSchemeDao.queryByGroupName(pointStrategyRequest.getAppGroupName(),
                pointStrategyRequest.getAppName(), pointStrategyRequest.getSdsSchemeName());
        if (sdsSchemeDO == null) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "降级预案" + pointStrategyRequest.
                    getSdsSchemeName() + "不存在！");
        }

        List<PointStrategyDO> pointStrategyDOS = pointStrategyDao.queryPointStrategyBySdsScheme(
                pointStrategyRequest.getAppGroupName(), pointStrategyRequest.getAppName(),
                pointStrategyRequest.getPoint(), pointStrategyRequest.getSdsSchemeName());
        if (CollectionUtils.isNotEmpty(pointStrategyDOS)) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "降级点" + pointStrategyRequest.getPoint() +
                    "和降级预案" + pointStrategyRequest.getSdsSchemeName() + "已存在！");
        }

        pointStrategyRequest.setOperatorName(pointStrategyRequest.getCreatorName());
        pointStrategyRequest.setOperatorEmail(pointStrategyRequest.getCreatorEmail());
        int result = pointStrategyDao.addPointStrategy(FastBeanUtil.copyForNew(pointStrategyRequest,
                new PointStrategyDO()));

        if (result == 1) {
            appInfoDao.increaseAppVersion(pointStrategyRequest.getAppGroupName(), pointStrategyRequest.getAppName(),
                    pointStrategyRequest.getCreatorName(), pointStrategyRequest.getCreatorEmail());
        }

        return result == 1 ? new SdsResponse(SdsCode.SUCCESS.getCode(), "新增降级点策略成功") :
                new SdsResponse(507, "新增降级点策略失败");
    }

    @RequestMapping(value = "edit")
    public SdsResponse updatePointStrategy(@RequestBody PointStrategyRequest pointStrategyRequest,
                                           HttpServletRequest request) {

        if (pointStrategyRequest == null) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(pointStrategyRequest.getAppGroupName())) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(pointStrategyRequest.getAppName())) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        if (StringUtils.isBlank(pointStrategyRequest.getSdsSchemeName())) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "降级预案名称不能为空");
        }

        if (StringUtils.isBlank(pointStrategyRequest.getNewSdsSchemeName())) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "新降级预案名称不能为空");
        }

        if (StringUtils.isBlank(pointStrategyRequest.getPoint())) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "降级点不能为空");
        }

        if (pointStrategyRequest.getDowngradeRate() == null) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "降级比例不能为空");
        }

        if (pointStrategyRequest.getStatus() == null) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "状态不能为空");
        }

//        if (StringUtils.isBlank(pointStrategyRequest.getOperatorName())) {
//            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "操作者姓名不能为空");
//        }
//
//        if (StringUtils.isBlank(pointStrategyRequest.getOperatorEmail())) {
//            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "操作者邮箱不能为空");
//        }

        if (pointStrategyRequest.getPoint().length() > 200) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "降级点名称不能超过200个字符");
        }

        List<PointStrategyDO> oldPointStrategyDOS =
                pointStrategyDao.queryPointStrategyBySdsScheme(pointStrategyRequest.getAppGroupName(),
                        pointStrategyRequest.getAppName(), pointStrategyRequest.getPoint(),
                        pointStrategyRequest.getSdsSchemeName());
        if (CollectionUtils.isEmpty(oldPointStrategyDOS)) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "降级点" + pointStrategyRequest.getPoint() + "和降级预案" +
                    pointStrategyRequest.getSdsSchemeName() + "不存在！");
        }

        if (!Objects.equals(pointStrategyRequest.getSdsSchemeName(),
                pointStrategyRequest.getNewSdsSchemeName())) {
            List<PointStrategyDO> pointStrategyDOS =
                    pointStrategyDao.queryPointStrategyBySdsScheme(pointStrategyRequest.getAppGroupName(),
                            pointStrategyRequest.getAppName(), pointStrategyRequest.getPoint(),
                            pointStrategyRequest.getNewSdsSchemeName());
            if (CollectionUtils.isNotEmpty(pointStrategyDOS)) {
                return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "降级点" + pointStrategyRequest.getPoint() + "和降级预案" +
                        pointStrategyRequest.getNewSdsSchemeName() + "已存在！");
            }
        }

        fillNullValue(pointStrategyRequest);

        PointStrategyDO savePointStrategyDO = FastBeanUtil.copyForNew(pointStrategyRequest, new PointStrategyDO());

        int result = pointStrategyDao.updatePointStrategy(savePointStrategyDO);

        if (result == 1) {
            appInfoDao.increaseAppVersion(pointStrategyRequest.getAppGroupName(), pointStrategyRequest.getAppName(),
                    pointStrategyRequest.getOperatorName(), pointStrategyRequest.getOperatorEmail());
        }

        return result == 1 ? new SdsResponse(SdsCode.SUCCESS.getCode(), "修改降级点策略成功") : new SdsResponse(508,
                "修改降级点策略失败");
    }

    @RequestMapping(value = "delete")
    public SdsResponse deletePointStrategy(@RequestBody PointStrategyRequest pointStrategyRequest) {

        if (pointStrategyRequest == null) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        int result = pointStrategyDao.deletePointStrategy(pointStrategyRequest.getAppGroupName(),
                pointStrategyRequest.getAppName(),
                pointStrategyRequest.getPoint(), pointStrategyRequest.getSdsSchemeName());

        if (result == 1) {
            appInfoDao.increaseAppVersion(pointStrategyRequest.getAppGroupName(), pointStrategyRequest.getAppName(),
                    pointStrategyRequest.getOperatorName(), pointStrategyRequest.getOperatorEmail());
        }

        return result == 1 ? new SdsResponse(SdsCode.SUCCESS.getCode(), "删除降级点策略成功") : new SdsResponse(509,
                "删除降级点策略失败");
    }

    /**
     * 保证里面没有null值，如果有，那么填上默认值
     *
     * @param pointStrategyRequest
     */
    private void fillNullValue(PointStrategyRequest pointStrategyRequest) {
        if (pointStrategyRequest == null) {
            return;
        }

        if (pointStrategyRequest.getVisitThreshold() == null) {
            pointStrategyRequest.setVisitThreshold(-1L);
        }

        if (pointStrategyRequest.getConcurrentThreshold() == null) {
            pointStrategyRequest.setConcurrentThreshold(-1);
        }

        if (pointStrategyRequest.getExceptionThreshold() == null) {
            pointStrategyRequest.setExceptionThreshold(-1L);
        }

        if (pointStrategyRequest.getTimeoutThreshold() == null) {
            pointStrategyRequest.setTimeoutThreshold(-1L);
        }

        if (pointStrategyRequest.getTimeoutCountThreshold() == null) {
            pointStrategyRequest.setTimeoutCountThreshold(-1L);
        }

        if (pointStrategyRequest.getExceptionRateThreshold() == null) {
            pointStrategyRequest.setExceptionRateThreshold(-1);
        }

        if (pointStrategyRequest.getExceptionRateStart() == null) {
            pointStrategyRequest.setExceptionRateStart(-1L);
        }

        if (pointStrategyRequest.getTokenBucketGeneratedTokensInSecond() == null) {
            pointStrategyRequest.setTokenBucketGeneratedTokensInSecond(-1);
        }

        if (pointStrategyRequest.getTokenBucketSize() == null) {
            pointStrategyRequest.setTokenBucketSize(-1);
        }

        if (pointStrategyRequest.getDelayTime() == null) {
            pointStrategyRequest.setDelayTime(-1L);
        }

        if (pointStrategyRequest.getRetryInterval() == null) {
            pointStrategyRequest.setRetryInterval(-1L);
        }
    }

    @ExceptionHandler
    public void handler(Exception e) {
        logger.error("PointStrategyController has exception.", e);
    }
}
