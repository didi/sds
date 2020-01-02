package com.didiglobal.sds.web.service;

import com.didiglobal.sds.client.bean.SdsCycleInfo;
import com.didiglobal.sds.web.dao.bean.PointDictDO;

import java.util.List;
import java.util.Map;

/**
 * 降级点字典表数据新增
 * Created by tianyulei on 2019/7/10
 **/
public interface PointDictService {

    /**
     * 如果降级点不存在则新增
     *
     * @param appGroupName
     * @param appName
     * @param point
     */
    Boolean addIfNotExist(String appGroupName, String appName, String point);

    /**
     * 批量新增降级点
     * 新增前会判断降级点是否存在
     *
     * @param list
     * @return
     */
    void addPointList(List<PointDictDO> list);

    /**
     * 查询该应用所有降级字典列表
     *
     * @param appGroupName
     * @param appName
     * @return
     */
    List<PointDictDO> queryAllPointDictList(String appGroupName, String appName);

    /**
     * 分页查询该应用降级字典列表
     *
     * @param appGroupName
     * @param appName
     * @param point        该字段模糊查询
     * @param page
     * @param pageSize
     * @return
     */
    List<PointDictDO> queryPointDictListByPage(String appGroupName, String appName, String point, Integer page,
                                               Integer pageSize);

    /**
     * 检查应用中的降级点是否与库里一直,若库里的降级点更多,且满足一定周期,则认定其为无效降级点,需进行删除
     *
     * @param map
     * @param appGroupName
     * @param appName
     */
    void checkAndDeleteDeadPoint(Map<String, SdsCycleInfo> map, String appGroupName, String appName);
}
