package com.didiglobal.sds.admin.dao;


import com.didiglobal.sds.admin.dao.bean.PointReturnValueDO;
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
public interface PointReturnValueDao {

    /**
     * 新增降级点返回值
     *
     * @param pointReturnValueDO
     * @return
     */
    @Insert("insert into point_return_value(app_group_name, app_name, point, return_value_str, status, creator_name, "
            + "creator_email, operator_name, operator_email) " +
            "values(#{appGroupName}, #{appName}, #{point}, #{returnValueStr}, #{status}, #{creatorName}, "
            + "#{creatorEmail}, #{operatorName}, #{operatorEmail})")
    int addPointReturnValue(PointReturnValueDO pointReturnValueDO);

    /**
     * 更新降级点返回值
     *
     * @param appGroupName
     * @param appName
     * @param point
     * @param newReturnValueStr
     * @param newStatus
     * @param operatorName
     * @param operatorEmail
     * @return
     */
    @Update("update point_return_value set " +
            "return_value_str = #{newReturnValueStr}, " +
            "status = #{newStatus}, " +
            "operator_name = #{operatorName}, operator_email = #{operatorEmail} " +
            "where app_group_name = #{appGroupName} and app_name = #{appName} and point = #{point}")
    int updatePointReturnValue(@Param("appGroupName") String appGroupName, @Param("appName") String appName, @Param(
            "point") String point,
                               @Param("newReturnValueStr") String newReturnValueStr,
                               @Param("newStatus") Integer newStatus,
                               @Param("operatorName") String operatorName,
                               @Param("operatorEmail") String operatorEmail);

    /**
     * 删除降级点返回值
     *
     * @param appGroupName
     * @param appName
     * @param point
     * @return
     */
    @Update("delete from point_return_value where point = #{point}")
    int deletePointReturnValue(@Param("appGroupName") String appGroupName, @Param("appName") String appName, @Param(
            "point") String point);

    /**
     * 查询所有降级点返回值
     *
     * @return
     */
    @Select("select * from point_return_value order by modify_time desc ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "point", column = "point"),
            @Result(property = "returnValueStr", column = "return_value_str"),
            @Result(property = "operatorName", column = "operator_name"),
            @Result(property = "operatorEmail", column = "operator_email"),
            @Result(property = "creatorName", column = "creator_name"),
            @Result(property = "creatorEmail", column = "creator_email"),
            @Result(property = "modifiedTime", column = "modify_time"),
            @Result(property = "createTime", column = "create_time")
    })
    List<PointReturnValueDO> queryAllPointReturnValue();

    /**
     * 查询所有降级点返回值
     *
     * @return
     */
    @Select("<script> select * from point_return_value <where> <trim prefixOverrides=\"and\">" +
            "<if test=\"appGroupName != null and appGroupName.length > 0 \"> app_group_name = #{appGroupName} </if>" +
            "<if test=\"appName != null and appName.length > 0 \"> and app_name = #{appName}  </if>" +
            "<if test=\"point != null and point.length > 0 \"> and point like concat('%', #{point}, '%') </if>" +
            "<if test=\"returnValueStr != null and returnValueStr.length > 0 \"> and return_value_str like concat"
            + "('%', #{returnValueStr}, '%') </if>" +
            "</trim> </where>" +
            " order by modify_time desc " +
            " limit #{start}, #{size} </script> ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "point", column = "point"),
            @Result(property = "returnValueStr", column = "return_value_str"),
            @Result(property = "operatorName", column = "operator_name"),
            @Result(property = "operatorEmail", column = "operator_email"),
            @Result(property = "creatorName", column = "creator_name"),
            @Result(property = "creatorEmail", column = "creator_email"),
            @Result(property = "modifiedTime", column = "modify_time"),
            @Result(property = "createTime", column = "create_time")
    })
    List<PointReturnValueDO> queryPointReturnValueByPage(@Param("appGroupName") String appGroupName,
                                                         @Param("appName") String appName,
                                                         @Param("point") String point,
                                                         @Param("returnValueStr") String returnValueStr,
                                                         @Param("start") Integer start, @Param("size") Integer size);

    /**
     * 通过降级点来查询
     *
     * @param point
     * @return
     */
    @Select("select * from point_return_value where app_group_name = #{appGroupName} and app_name = #{appName}  " +
            "and point = #{point}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "point", column = "point"),
            @Result(property = "returnValueStr", column = "return_value_str"),
            @Result(property = "operatorName", column = "operator_name"),
            @Result(property = "operatorEmail", column = "operator_email"),
            @Result(property = "creatorName", column = "creator_name"),
            @Result(property = "creatorEmail", column = "creator_email"),
            @Result(property = "modifiedTime", column = "modify_time"),
            @Result(property = "createTime", column = "create_time")
    })
    PointReturnValueDO queryByPoint(@Param("appGroupName") String appGroupName, @Param("appName") String appName,
                                    @Param("point") String point);

    /**
     * 批量查询降级点返回值
     *
     * @param appGroupName
     * @param appName
     * @param points
     * @param status
     * @return
     */
    @Select(" <script> select * from point_return_value where app_group_name = #{appGroupName} " +
            " and app_name = #{appName} " +
            " <if test='points != null'> " +
            " and point in " +
            " <foreach collection=\"points\" item=\"point\" open=\"(\" close=\")\" separator=\",\">#{point}</foreach>" +
            "  </if> " +
            " and status = #{status} " +
            " </script> ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "point", column = "point"),
            @Result(property = "returnValueStr", column = "return_value_str"),
            @Result(property = "operatorName", column = "operator_name"),
            @Result(property = "operatorEmail", column = "operator_email"),
            @Result(property = "creatorName", column = "creator_name"),
            @Result(property = "creatorEmail", column = "creator_email"),
            @Result(property = "modifiedTime", column = "modify_time"),
            @Result(property = "createTime", column = "create_time")
    })
    List<PointReturnValueDO> queryPointReturnValueBatch(@Param("appGroupName") String appGroupName,
                                                        @Param("appName") String appName,
                                                        @Param("points") List<String> points,
                                                        @Param("status") Integer status);
}
