package com.didiglobal.sds.admin.dao;

import com.didiglobal.sds.admin.dao.bean.PointStrategyDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by manzhizhen on 18/2/11.
 */
@Mapper
public interface PointStrategyDao {

    /**
     * 新增降级点策略
     *
     * @param strategyDO
     * @return
     */
    @Insert("<script> insert into point_strategy (app_group_name, app_name, point, sds_scheme_name, "
            + "downgrade_rate, status, operator_name, operator_email, creator_name, creator_email" +
            "<if test='visitThreshold != null'> , visit_threshold </if> " +
            "<if test='concurrentThreshold != null'> , concurrent_threshold </if> " +
            "<if test='exceptionThreshold != null'> , exception_threshold </if> " +
            "<if test='exceptionRateThreshold != null'> , exception_rate_threshold </if> " +
            "<if test='exceptionRateStart != null'> , exception_rate_start </if> " +
            "<if test='timeoutThreshold != null'> , timeout_threshold </if> " +
            "<if test='timeoutCountThreshold != null'> , timeout_count_threshold </if> " +
            "<if test='tokenBucketGeneratedTokensInSecond != null'> , token_bucket_generated_tokens_in_second </if> " +
            "<if test='tokenBucketSize != null'> , token_bucket_size </if> " +
            "<if test='delayTime != null'> , delay_time </if> " +
            "<if test='pressureTestDowngrade != null'> , pressure_test_downgrade </if> " +
            "<if test='retryInterval != null'> , retry_interval </if> ) " +
            " values(#{appGroupName}, #{appName}, #{point}, #{sdsSchemeName}, #{downgradeRate}, #{status}, "
            + "#{operatorName}, #{operatorEmail}, #{creatorName}, #{creatorEmail}" +
            "<if test='visitThreshold != null'> , #{visitThreshold}  </if> " +
            "<if test='concurrentThreshold != null'> , #{concurrentThreshold} </if> " +
            "<if test='exceptionThreshold != null'> , #{exceptionThreshold} </if> " +
            "<if test='exceptionRateThreshold != null'> , #{exceptionRateThreshold} </if> " +
            "<if test='exceptionRateStart != null'> , #{exceptionRateStart} </if> " +
            "<if test='timeoutThreshold != null'> , #{timeoutThreshold} </if> " +
            "<if test='timeoutCountThreshold != null'> , #{timeoutCountThreshold} </if> " +
            "<if test='tokenBucketGeneratedTokensInSecond != null'> , #{tokenBucketGeneratedTokensInSecond} </if> " +
            "<if test='tokenBucketSize != null'> , #{tokenBucketSize} </if> " +
            "<if test='delayTime != null'> , #{delayTime} </if> " +
            "<if test='pressureTestDowngrade != null'> , #{pressureTestDowngrade} </if> " +
            "<if test='retryInterval != null'> , #{retryInterval} </if> )" +
            " </script>")
    int addPointStrategy(PointStrategyDO strategyDO);

    /**
     * 批量新增降级点策略
     *
     * @param strategyDOList
     * @return
     */
    @Insert("<script> insert into point_strategy (app_group_name, app_name, point, sds_scheme_name, "
            + "visit_threshold, " +
            "concurrent_threshold, exception_threshold, exception_rate_threshold, exception_rate_start, "
            + "timeout_threshold, " +
            "timeout_count_threshold, token_bucket_generated_tokens_in_second, token_bucket_size, delay_time, "
            + "retry_interval, downgrade_rate, pressure_test_downgrade, status, operator_name, operator_email, "
            + "creator_name, creator_email) " +
            "values " +
            "<foreach collection=\"strategyDOList\" item=\"strategyDO\" separator=\",\" open=\"(\" close=\")\" >" +
            "#{strategyDO.appGroupName}, #{strategyDO.appName}, #{strategyDO.point}, #{strategyDO.sdsSchemeName},"
            + " #{strategyDO.visitThreshold}, #{strategyDO.concurrentThreshold}, " +
            "#{strategyDO.exceptionThreshold}, #{strategyDO.exceptionRateThreshold}, #{strategyDO"
            + ".exceptionRateStart}, #{strategyDO.timeoutThreshold}, #{strategyDO.timeoutCountThreshold}, " +
            "#{strategyDO.tokenBucketGeneratedTokensInSecond}, #{strategyDO.tokenBucketSize}, #{strategyDO"
            + ".delayTime}, #{strategyDO.retryInterval}, #{strategyDO.downgradeRate},  #{strategyDO"
            + ".pressureTestDowngrade}, #{strategyDO.status}, " +
            "#{strategyDO.operatorName}, #{strategyDO.operatorEmail}, #{strategyDO.creatorName}, #{strategyDO"
            + ".creatorEmail}" +
            " </foreach>" +
            " </script>")
    int addPointStrategyBatch(List<PointStrategyDO> strategyDOList);

    /**
     * 删除降级点策略
     *
     * @param appGroupName
     * @param appName
     * @param point
     * @param sdsSchemeName
     * @return
     */
    @Delete("delete from point_strategy " +
            " where app_group_name = #{appGroupName}" +
            " and app_name = #{appName}" +
            " and point = #{point}" +
            " and sds_scheme_name = #{sdsSchemeName}")
    int deletePointStrategy(@Param("appGroupName") String appGroupName, @Param("appName") String appName, @Param(
            "point") String point,
                            @Param("sdsSchemeName") String sdsSchemeName);

    /**
     * 更新降级点策略
     *
     * @param strategyDO
     * @return
     */
    @Update("<script> update point_strategy <set> " +
            " sds_scheme_name = #{newSdsSchemeName}, status = #{status}, operator_name = #{operatorName}, "
            + "operator_email = #{operatorEmail}  " +
            "<if test='visitThreshold != null'> , visit_threshold = #{visitThreshold} </if> " +
            "<if test='concurrentThreshold != null'> , concurrent_threshold = #{concurrentThreshold} </if> " +
            "<if test='exceptionThreshold != null'> , exception_threshold = #{exceptionThreshold} </if> " +
            "<if test='exceptionRateThreshold != null'> , exception_rate_threshold = #{exceptionRateThreshold} </if> " +
            "<if test='exceptionRateStart != null'> , exception_rate_start = #{exceptionRateStart} </if> " +
            "<if test='timeoutThreshold != null'> , timeout_threshold = #{timeoutThreshold} </if> " +
            "<if test='timeoutCountThreshold != null'> , timeout_count_threshold = #{timeoutCountThreshold} </if> " +
            "<if test='tokenBucketGeneratedTokensInSecond != null'> , token_bucket_generated_tokens_in_second = "
            + "#{tokenBucketGeneratedTokensInSecond} </if> " +
            "<if test='tokenBucketSize != null'> , token_bucket_size = #{tokenBucketSize} </if> " +
            "<if test='delayTime != null'> , delay_time = #{delayTime} </if> " +
            "<if test='retryInterval != null'> , retry_interval = #{retryInterval} </if> " +
            "<if test='downgradeRate != null'> , downgrade_rate = #{downgradeRate} </if> " +
            "<if test='pressureTestDowngrade != null'> , pressure_test_downgrade = #{pressureTestDowngrade} </if> " +
            " </set> " +
            " where app_group_name = #{appGroupName}" +
            " and app_name = #{appName}" +
            " and point = #{point}" +
            " and sds_scheme_name = #{sdsSchemeName}" +
            " </script>")
    int updatePointStrategy(PointStrategyDO strategyDO);

    /**
     * 批量查询降级点策略
     *
     * @param appGroupName
     * @param appName
     * @param points
     * @param sdsSchemeName
     * @return
     */
    @Select("<script> select * from point_strategy where app_group_name = #{appGroupName} " +
            " and app_name = #{appName} " +
            " and sds_scheme_name = #{sdsSchemeName} " +
            " <if test='points != null'> " +
            " and point in " +
            " <foreach collection=\"points\" item=\"point\" open=\"(\" close=\")\" separator=\",\">#{point}</foreach>" +
            " </if> " +
            " and status = 1 </script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "point", column = "point"),
            @Result(property = "sdsSchemeName", column = "sds_scheme_name"),
            @Result(property = "visitThreshold", column = "visit_threshold"),
            @Result(property = "concurrentThreshold", column = "concurrent_threshold"),
            @Result(property = "exceptionThreshold", column = "exception_threshold"),
            @Result(property = "exceptionRateThreshold", column = "exception_rate_threshold"),
            @Result(property = "exceptionRateStart", column = "exception_rate_start"),
            @Result(property = "timeoutThreshold", column = "timeout_threshold"),
            @Result(property = "timeoutCountThreshold", column = "timeout_count_threshold"),
            @Result(property = "tokenBucketGeneratedTokensInSecond", column =
                    "token_bucket_generated_tokens_in_second"),
            @Result(property = "tokenBucketSize", column = "token_bucket_size"),
            @Result(property = "delayTime", column = "delay_time"),
            @Result(property = "retryInterval", column = "retry_interval"),
            @Result(property = "downgradeRate", column = "downgrade_rate"),
            @Result(property = "pressureTestDowngrade", column = "pressure_test_downgrade"),
            @Result(property = "status", column = "status"),
            @Result(property = "operatorName", column = "operator_name"),
            @Result(property = "operatorEmail", column = "operator_email"),
            @Result(property = "modifiedTime", column = "modify_time"),
            @Result(property = "createTime", column = "create_time")
    })
    List<PointStrategyDO> queryPointStrategyBatch(@Param("appGroupName") String appGroupName,
                                                  @Param("appName") String appName,
                                                  @Param("points") List<String> points,
                                                  @Param("sdsSchemeName") String sdsSchemeName);

    /**
     * 分页查询降级点策略
     *
     * @param appGroupName
     * @param appName
     * @param point
     * @param sdsSchemeName
     * @param start
     * @param size
     * @return
     */
    @Select("<script> select * from point_strategy " +
            "<where> " +
            " <if test='appGroupName != null and appGroupName.length > 0'> and app_group_name = #{appGroupName} </if>" +
            " <if test='appName != null and appName.length > 0'> and app_name = #{appName} </if> " +
            " <if test='sdsSchemeName != null and sdsSchemeName.length > 0'> and sds_scheme_name = "
            + "#{sdsSchemeName} </if>" +
            " <if test='point != null and point.length > 0'> and point like concat('%', #{point}, '%') </if> " +
            "</where>" +
            " order by modify_time desc " +
            " limit #{start}, #{size} </script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "point", column = "point"),
            @Result(property = "sdsSchemeName", column = "sds_scheme_name"),
            @Result(property = "visitThreshold", column = "visit_threshold"),
            @Result(property = "concurrentThreshold", column = "concurrent_threshold"),
            @Result(property = "exceptionThreshold", column = "exception_threshold"),
            @Result(property = "exceptionRateThreshold", column = "exception_rate_threshold"),
            @Result(property = "exceptionRateStart", column = "exception_rate_start"),
            @Result(property = "timeoutThreshold", column = "timeout_threshold"),
            @Result(property = "timeoutCountThreshold", column = "timeout_count_threshold"),
            @Result(property = "tokenBucketGeneratedTokensInSecond", column =
                    "token_bucket_generated_tokens_in_second"),
            @Result(property = "tokenBucketSize", column = "token_bucket_size"),
            @Result(property = "delayTime", column = "delay_time"),
            @Result(property = "retryInterval", column = "retry_interval"),
            @Result(property = "downgradeRate", column = "downgrade_rate"),
            @Result(property = "pressureTestDowngrade", column = "pressure_test_downgrade"),
            @Result(property = "status", column = "status"),
            @Result(property = "operatorName", column = "operator_name"),
            @Result(property = "operatorEmail", column = "operator_email"),
            @Result(property = "creatorName", column = "creator_name"),
            @Result(property = "creatorEmail", column = "creator_email"),
            @Result(property = "modifiedTime", column = "modify_time"),
            @Result(property = "createTime", column = "create_time")
    })
    List<PointStrategyDO> queryPointStrategyByPage(@Param("appGroupName") String appGroupName,
                                                   @Param("appName") String appName, @Param("point") String point,
                                                   @Param("sdsSchemeName") String sdsSchemeName, @Param(
                                                           "start") Integer start, @Param("size") Integer size);

    /**
     * 查询该降级预案下有多少策略
     *
     * @param appGroupName
     * @param appName
     * @param point
     * @param sdsSchemeName
     * @param start
     * @param size
     * @return
     */
    @Select("select count(*) from point_strategy where app_group_name = #{appGroupName} and app_name = #{appName} and"
            + " sds_scheme_name = #{sdsSchemeName}")
    int queryPointStrategyCountBySdsScheme(@Param("appGroupName") String appGroupName,
                                               @Param("appName") String appName,
                                               @Param("sdsSchemeName") String sdsSchemeName);

    /**
     * 通过降级预案查询降级点策略
     *
     * @param appGroupName
     * @param appName
     * @param point
     * @param sdsSchemeName 值为null时表示查询该应用下面的所有降级点
     * @return
     */
    @Select("<script>  " +
            " select * from point_strategy " +
            " where app_group_name = #{appGroupName} " +
            " and app_name = #{appName} " +
            " <if test='point != null'> and point = #{point} </if>" +
            " <if test='sdsSchemeName != null'> and sds_scheme_name = #{sdsSchemeName} </if>" +
            " order by modify_time desc " +
            "</script> ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "point", column = "point"),
            @Result(property = "sdsSchemeName", column = "sds_scheme_name"),
            @Result(property = "visitThreshold", column = "visit_threshold"),
            @Result(property = "concurrentThreshold", column = "concurrent_threshold"),
            @Result(property = "exceptionThreshold", column = "exception_threshold"),
            @Result(property = "exceptionRateThreshold", column = "exception_rate_threshold"),
            @Result(property = "exceptionRateStart", column = "exception_rate_start"),
            @Result(property = "timeoutThreshold", column = "timeout_threshold"),
            @Result(property = "timeoutCountThreshold", column = "timeout_count_threshold"),
            @Result(property = "tokenBucketGeneratedTokensInSecond", column =
                    "token_bucket_generated_tokens_in_second"),
            @Result(property = "tokenBucketSize", column = "token_bucket_size"),
            @Result(property = "delayTime", column = "delay_time"),
            @Result(property = "retryInterval", column = "retry_interval"),
            @Result(property = "downgradeRate", column = "downgrade_rate"),
            @Result(property = "pressureTestDowngrade", column = "pressure_test_downgrade"),
            @Result(property = "status", column = "status"),
            @Result(property = "operatorName", column = "operator_name"),
            @Result(property = "operatorEmail", column = "operator_email"),
            @Result(property = "modifiedTime", column = "modify_time"),
            @Result(property = "createTime", column = "create_time")
    })
    List<PointStrategyDO> queryPointStrategyBySdsScheme(@Param("appGroupName") String appGroupName, @Param(
            "appName") String appName,
                                                            @Param("point") String point,
                                                            @Param("sdsSchemeName") String sdsSchemeName);

}
