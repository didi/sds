package com.didiglobal.sds.admin.dao;

import com.didiglobal.sds.admin.dao.bean.SdsSchemeDO;
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
public interface SdsSchemeDao {

    /**
     * 新增降级预案
     *
     * @param sdsSchemeDO
     * @return
     */
    @Insert("insert into sds_scheme(app_group_name, app_name, sds_scheme_name, operator_name, operator_email,"
            + " creator_name, creator_email) " +
            "values(#{appGroupName}, #{appName}, #{sdsSchemeName}, #{operatorName}, #{operatorEmail}, "
            + "#{creatorName}, #{creatorEmail})")
    int addSdsScheme(SdsSchemeDO sdsSchemeDO);


    /**
     * 更新降级预案名字
     *
     * @param appGroupName
     * @param appName
     * @param sdsSchemeName
     * @param newSdsSchemeName
     * @param operatorName
     * @param operatorEmail
     * @return
     */
    @Update("update sds_scheme set sds_scheme_name = #{newSdsSchemeName}, operator_name = "
            + "#{operatorName}, operator_email = #{operatorEmail} " +
            " where app_group_name = #{appGroupName} and app_name = #{appName} and sds_scheme_name = "
            + "#{sdsSchemeName}")
    int updateSdsScheme(@Param("appGroupName") String appGroupName, @Param("appName") String appName, @Param(
            "sdsSchemeName") String sdsSchemeName,
                            @Param("newSdsSchemeName") String newSdsSchemeName,
                            @Param("operatorName") String operatorName, @Param("operatorEmail") String operatorEmail);

    /**
     * 批量更新降级预案的应用名称
     *
     * @param appGroupName
     * @param appName
     * @param newAppName
     * @param operatorName
     * @param operatorEmail
     * @return
     */
    @Update("update sds_scheme set app_name = #{newAppName}, operator_name = #{operatorName}, operator_email = "
            + "#{operatorEmail}  where app_group_name = #{appGroupName} and app_name = #{appName}")
    int updateSdsSchemeAppNameBatch(@Param("appGroupName") String appGroupName, @Param("appName") String appName,
                                        @Param("newAppName") String newAppName,
                                        @Param("operatorName") String operatorName,
                                        @Param("operatorEmail") String operatorEmail);


    /**
     * 删除降级预案
     *
     * @param appGroupName
     * @param appName
     * @param sdsSchemeName
     * @return
     */
    @Update("delete from sds_scheme " +
            "where app_group_name = #{appGroupName}" +
            " and app_name = #{appName}" +
            " and sds_scheme_name = #{sdsSchemeName}"
    )
    int deleteSdsScheme(@Param("appGroupName") String appGroupName, @Param("appName") String appName, @Param(
            "sdsSchemeName") String sdsSchemeName);

    /**
     * 查询所有降级预案
     *
     * @param appGroupName
     * @param appName
     * @return
     */
    @Select("select * from sds_scheme where app_group_name = #{appGroupName} and app_name = #{appName} order by "
            + "modify_time desc ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "sdsSchemeName", column = "sds_scheme_name"),
            @Result(property = "operatorName", column = "operator_name"),
            @Result(property = "operatorEmail", column = "operator_email"),
            @Result(property = "modifiedTime", column = "modify_time"),
            @Result(property = "createTime", column = "create_time")
    })
    List<SdsSchemeDO> queryAllSdsScheme(@Param("appGroupName") String appGroupName,
                                                @Param("appName") String appName);

    /**
     * 通过应用组名称来查询
     *
     * @param appGroupName
     * @param appName
     * @param sdsSchemeName
     * @return
     */
    @Select("select * from sds_scheme where app_group_name = #{appGroupName} " +
            " and app_name = #{appName} " +
            " and sds_scheme_name = #{sdsSchemeName} " +
            " order by modify_time desc ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "sdsSchemeName", column = "sds_scheme_name"),
            @Result(property = "operatorName", column = "operator_name"),
            @Result(property = "operatorEmail", column = "operator_email"),
            @Result(property = "modifiedTime", column = "modify_time"),
            @Result(property = "createTime", column = "create_time")
    })
    SdsSchemeDO queryByGroupName(@Param("appGroupName") String appGroupName, @Param("appName") String appName,
                                     @Param("sdsSchemeName") String sdsSchemeName);

    /**
     * 分页查询降级预案信息
     *
     * @param appGroupName
     * @param appName
     * @param sdsSchemeName
     * @param start
     * @param size
     * @return
     */
    @Select("<script> select * from sds_scheme " +
            "<where> " +
            " <if test=\"appGroupName != null and appGroupName.length > 0 \"> app_group_name = #{appGroupName} </if> " +
            " <if test=\"appName != null and appName.length > 0 \"> and app_name = #{appName} </if> " +
            " <if test=\"sdsSchemeName != null and sdsSchemeName.length > 0\"> and sds_scheme_name like "
            + "concat('%', #{sdsSchemeName}, '%') </if> " +
            "</where>" +
            " order by modify_time desc " +
            " limit #{start}, #{size} </script> ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "sdsSchemeName", column = "sds_scheme_name"),
            @Result(property = "operatorName", column = "operator_name"),
            @Result(property = "operatorEmail", column = "operator_email"),
            @Result(property = "creatorName", column = "creator_name"),
            @Result(property = "creatorEmail", column = "creator_email"),
            @Result(property = "modifiedTime", column = "modify_time"),
            @Result(property = "createTime", column = "create_time")
    })
    List<SdsSchemeDO> querySdsSchemeByPage(@Param("appGroupName") String appGroupName,
                                                   @Param("appName") String appName,
                                                   @Param("sdsSchemeName") String sdsSchemeName, @Param(
                                                           "start") Integer start, @Param("size") Integer size);
}
