package com.didiglobal.sds.web.dao;

import com.didiglobal.sds.web.dao.bean.StrategyGroupDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by yizhenqiang on 18/2/11.
 */
@Mapper
public interface StrategyGroupDao {

    /**
     * 新增策略组
     *
     * @param strategyGroupDO
     * @return
     */
    @Insert("insert into strategy_group(app_group_name, app_name, strategy_group_name, operator_name, operator_email,"
            + " creator_name, creator_email) " +
            "values(#{appGroupName}, #{appName}, #{strategyGroupName}, #{operatorName}, #{operatorEmail}, "
            + "#{creatorName}, #{creatorEmail})")
    int addStrategyGroup(StrategyGroupDO strategyGroupDO);


    /**
     * 更新策略组名字
     *
     * @param appGroupName
     * @param appName
     * @param strategyGroupName
     * @param newStrategyGroupName
     * @param operatorName
     * @param operatorEmail
     * @return
     */
    @Update("update strategy_group set strategy_group_name = #{newStrategyGroupName}, operator_name = "
            + "#{operatorName}, operator_email = #{operatorEmail} " +
            " where app_group_name = #{appGroupName} and app_name = #{appName} and strategy_group_name = "
            + "#{strategyGroupName}")
    int updateStrategyGroup(@Param("appGroupName") String appGroupName, @Param("appName") String appName, @Param(
            "strategyGroupName") String strategyGroupName,
                            @Param("newStrategyGroupName") String newStrategyGroupName,
                            @Param("operatorName") String operatorName, @Param("operatorEmail") String operatorEmail);

    /**
     * 批量更新策略组的应用名称
     *
     * @param appGroupName
     * @param appName
     * @param newAppName
     * @param operatorName
     * @param operatorEmail
     * @return
     */
    @Update("update strategy_group set app_name = #{newAppName}, operator_name = #{operatorName}, operator_email = "
            + "#{operatorEmail}  where app_group_name = #{appGroupName} and app_name = #{appName}")
    int updateStrategyGroupAppNameBatch(@Param("appGroupName") String appGroupName, @Param("appName") String appName,
                                        @Param("newAppName") String newAppName,
                                        @Param("operatorName") String operatorName,
                                        @Param("operatorEmail") String operatorEmail);


    /**
     * 删除策略组
     *
     * @param appGroupName
     * @param appName
     * @param strategyGroupName
     * @return
     */
    @Update("delete from strategy_group " +
            "where app_group_name = #{appGroupName}" +
            " and app_name = #{appName}" +
            " and strategy_group_name = #{strategyGroupName}"
    )
    int deleteStrategyGroup(@Param("appGroupName") String appGroupName, @Param("appName") String appName, @Param(
            "strategyGroupName") String strategyGroupName);

    /**
     * 查询所有策略组
     *
     * @param appGroupName
     * @param appName
     * @return
     */
    @Select("select * from strategy_group where app_group_name = #{appGroupName} and app_name = #{appName} order by "
            + "modify_time desc ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "strategyGroupName", column = "strategy_group_name"),
            @Result(property = "operatorName", column = "operator_name"),
            @Result(property = "operatorEmail", column = "operator_email"),
            @Result(property = "modifiedTime", column = "modify_time"),
            @Result(property = "createTime", column = "create_time")
    })
    List<StrategyGroupDO> queryAllStrategyGroup(@Param("appGroupName") String appGroupName,
                                                @Param("appName") String appName);

    /**
     * 通过应用组名称来查询
     *
     * @param appGroupName
     * @param appName
     * @param strategyGroupName
     * @return
     */
    @Select("select * from strategy_group where app_group_name = #{appGroupName} " +
            " and app_name = #{appName} " +
            " and strategy_group_name = #{strategyGroupName} " +
            " order by modify_time desc ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "strategyGroupName", column = "strategy_group_name"),
            @Result(property = "operatorName", column = "operator_name"),
            @Result(property = "operatorEmail", column = "operator_email"),
            @Result(property = "modifiedTime", column = "modify_time"),
            @Result(property = "createTime", column = "create_time")
    })
    StrategyGroupDO queryByGroupName(@Param("appGroupName") String appGroupName, @Param("appName") String appName,
                                     @Param("strategyGroupName") String strategyGroupName);

    /**
     * 分页查询策略组信息
     *
     * @param appGroupName
     * @param appName
     * @param strategyGroupName
     * @param start
     * @param size
     * @return
     */
    @Select("<script> select * from strategy_group " +
            "<where> " +
            " <if test=\"appGroupName != null and appGroupName.length > 0 \"> app_group_name = #{appGroupName} </if> " +
            " <if test=\"appName != null and appName.length > 0 \"> and app_name = #{appName} </if> " +
            " <if test=\"strategyGroupName != null and strategyGroupName.length > 0\"> and strategy_group_name like "
            + "concat('%', #{strategyGroupName}, '%') </if> " +
            "</where>" +
            " order by modify_time desc " +
            " limit #{start}, #{size} </script> ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "strategyGroupName", column = "strategy_group_name"),
            @Result(property = "operatorName", column = "operator_name"),
            @Result(property = "operatorEmail", column = "operator_email"),
            @Result(property = "creatorName", column = "creator_name"),
            @Result(property = "creatorEmail", column = "creator_email"),
            @Result(property = "modifiedTime", column = "modify_time"),
            @Result(property = "createTime", column = "create_time")
    })
    List<StrategyGroupDO> queryStrategyGroupByPage(@Param("appGroupName") String appGroupName,
                                                   @Param("appName") String appName,
                                                   @Param("strategyGroupName") String strategyGroupName, @Param(
                                                           "start") Integer start, @Param("size") Integer size);
}
