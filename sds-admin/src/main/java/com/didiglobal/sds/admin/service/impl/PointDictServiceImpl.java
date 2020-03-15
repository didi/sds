package com.didiglobal.sds.admin.service.impl;

import com.didiglobal.sds.client.bean.SdsCycleInfo;
import com.didiglobal.sds.admin.dao.PointDictDao;
import com.didiglobal.sds.admin.dao.bean.PointDictDO;
import com.didiglobal.sds.admin.service.PointDictService;
import com.didiglobal.sds.admin.util.BloomFileter;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by tianyulei on 2019/7/10
 **/
@Service
public class PointDictServiceImpl implements PointDictService {

    private Logger logger = LoggerFactory.getLogger(PointDictServiceImpl.class);

    @Autowired
    private PointDictDao pointDictDao;

    private BloomFileter bloomFileter = new BloomFileter(10000);

    /**
     * 如果降级点不存在则新增
     *
     * @param appGroupName
     * @param appName
     * @param point
     */
    @Override
    public Boolean addIfNotExist(String appGroupName, String appName, String point) {
        if (checkExist(appGroupName, appName, point)) {
            return false;
        }

        PointDictDO pointDictDO = new PointDictDO();
        pointDictDO.setPoint(point);
        pointDictDO.setAppName(appName);
        pointDictDO.setAppGroupName(appGroupName);

        return pointDictDao.addPointDict(pointDictDO) > 0;
    }

    /**
     * 批量新增降级点
     * 新增前会判断降级点是否存在
     *
     * @param list
     * @return
     */
    @Override
    public void addPointList(List<PointDictDO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<PointDictDO> listInsert = Lists.newArrayList();
        for (PointDictDO pointDictDO : list) {
            if (checkExist(pointDictDO.getAppGroupName(), pointDictDO.getAppName(), pointDictDO.getPoint())) {
                continue;
            }
            listInsert.add(pointDictDO);
        }

        if (CollectionUtils.isEmpty(listInsert)) {
            return;
        }

        pointDictDao.addPointDictList(listInsert);
    }


    @Override
    public List<PointDictDO> queryAllPointDictList(String appGroupName, String appName) {
        return pointDictDao.queryAllPointDicts(appGroupName, appName);
    }

    @Override
    public List<PointDictDO> queryPointDictListByPage(String appGroupName, String appName, String point, Integer page
            , Integer pageSize) {
        page = page == null ? 1 : page;
        pageSize = pageSize == null ? 10 : pageSize;

        return pointDictDao.queryPointDictsByPage(appGroupName, appName, point, page, pageSize);
    }

    /**
     * 检查应用中的降级点是否与库里一致,若库里的降级点更多,且满足一定周期,则认定其为无效降级点,需进行删除
     *
     * @param map
     */
    @Override
    public void checkAndDeleteDeadPoint(Map<String, SdsCycleInfo> map, String appGroupName, String appName) {
        List<PointDictDO> pointDictDOS = queryAllPointDictList(appGroupName, appName);
        if (CollectionUtils.isEmpty(pointDictDOS)) {
            pointDictDOS = queryAllPointDictList(appGroupName, appName);
        }

        List<Long> deleteIds = Lists.newArrayList();
        for (PointDictDO pointDictDO : pointDictDOS) {
            if (!map.containsKey(pointDictDO.getPoint())) {
                deleteIds.add(pointDictDO.getId());
            }
        }

        if (CollectionUtils.isNotEmpty(deleteIds)) {
            pointDictDao.deletePointDictList(deleteIds);
        }
    }


    /**
     * 利用布隆过滤器加db校验降级点是否存在
     *
     * @param appGroupName
     * @param appName
     * @param point
     * @return
     */
    private boolean checkExist(String appGroupName, String appName, String point) {
        String key = appGroupName + appName + point;

        //布隆过滤器,新增成功说明记录不存在,由于是本地缓存,需要进行db的二次确认
        if (bloomFileter.addIfNotExist(key)) {
            PointDictDO pointDictDO = pointDictDao.queryPointDict(appGroupName, appName, point);
            if (pointDictDO == null) {
                return false;
            }
        }
        return true;
    }


}
