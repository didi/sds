package com.didiglobal.sds.admin.controller;

import com.didiglobal.sds.client.util.StringUtils;
import com.didiglobal.sds.admin.constants.SdsCode;
import com.didiglobal.sds.admin.controller.request.PointDictRequest;
import com.didiglobal.sds.admin.controller.response.SdsResponse;
import com.didiglobal.sds.admin.dao.bean.PointDictDO;
import com.didiglobal.sds.admin.service.PointDictService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by tianyulei on 2019/8/11
 **/
@RestController
@RequestMapping(value = "/sds/pointdict/", method = RequestMethod.POST)
public class PointDictController {

    @Autowired
    private PointDictService pointDictService;

    @RequestMapping(value = "querylist")
    public SdsResponse<List<String>> queryPointDictList(@RequestBody PointDictRequest request) {
        if (request == null) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "请求参数错误，请联系程序猿");
        }

        if (StringUtils.isBlank(request.getAppGroupName())) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "应用组名称不能为空");
        }

        if (StringUtils.isBlank(request.getAppName())) {
            return new SdsResponse(SdsCode.PARAM_ERROR.getCode(), "应用名称不能为空");
        }

        List<PointDictDO> list = pointDictService.queryAllPointDictList(request.getAppGroupName(), request.getAppName());

        return new SdsResponse(Lists.transform(list, (pointDictDO) -> pointDictDO.getPoint()));
    }
}
