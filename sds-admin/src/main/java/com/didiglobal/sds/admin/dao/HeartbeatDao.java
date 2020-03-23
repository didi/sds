package com.didiglobal.sds.admin.dao;

import com.didiglobal.sds.admin.conditional.MultConditionalOnProperty;
import com.didiglobal.sds.admin.dao.bean.HeartbeatDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 心跳的DAO，心跳数据用于给仪表盘来展示数据
 * 注意：只有使用mysql或h2时才初始化该DAO
 * Created by manzhizhen on 18/7/1.
 */
@Mapper
@MultConditionalOnProperty(name = "sds.heartbeat.database.type", havingValue = {"mysql", "h2"})
public interface HeartbeatDao {

    /**
     * 新增心跳数据
     *
     * @param heartbeatDOList
     * @return
     */
    @Insert("<script> insert into heartbeat(app_group_name, app_name, point, downgrade_num, visit_num, exception_num,"
            + " timeout_num, max_concurrent_num, app_ip, statistics_cycle_time)" +
            " values" +
            " <foreach collection=\"heartbeatDOList\" item=\"heartbeatDO\" separator=\",\">" +
            "( #{heartbeatDO.appGroupName}, #{heartbeatDO.appName}, #{heartbeatDO.point}, #{heartbeatDO"
            + ".downgradeNum}, #{heartbeatDO.visitNum}, " +
            "#{heartbeatDO.exceptionNum}, #{heartbeatDO.timeoutNum}, #{heartbeatDO.maxConcurrentNum}, #{heartbeatDO"
            + ".appIp}, #{heartbeatDO.statisticsCycleTime} )" +
            "</foreach> </script>")
    int addHeartbeat(@Param("heartbeatDOList") List<HeartbeatDO> heartbeatDOList);

    /**
     * 按条件查询心跳数据
     *
     * @param appGroupName
     * @param appName
     * @param point
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select * from heartbeat where app_group_name = #{appGroupName} " +
            " and app_name = #{appName} " +
            " and point = #{point} " +
            " and statistics_cycle_time >= #{startTime}" +
            " and statistics_cycle_time <= #{endTime}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "point", column = "point"),
            @Result(property = "downgradeNum", column = "downgrade_num"),
            @Result(property = "visitNum", column = "visit_num"),
            @Result(property = "exceptionNum", column = "exception_num"),
            @Result(property = "timeoutNum", column = "timeout_num"),
            @Result(property = "maxConcurrentNum", column = "max_concurrent_num"),
            @Result(property = "appIp", column = "app_ip"),
            @Result(property = "statisticsCycleTime", column = "statistics_cycle_time")
    })
    List<HeartbeatDO> queryHeartbeatList(@Param("appGroupName") String appGroupName, @Param("appName") String appName,
                                         @Param("point") String point, @Param("startTime") Date startTime, @Param(
                                                 "endTime") Date endTime);

}
