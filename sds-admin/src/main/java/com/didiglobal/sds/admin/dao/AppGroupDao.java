package com.didiglobal.sds.admin.dao;

import com.didiglobal.sds.admin.dao.bean.AppGroupDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by manzhizhen on 18/2/11.
 */
@Mapper
public interface AppGroupDao {

    /**
     * 新增应用组
     *
     * @param appGroupDO
     * @return
     */
    @Insert("insert into app_group(app_group_name, creator_name, creator_email, operator_name, operator_email) " +
            "values(#{appGroupName}, #{creatorName}, #{creatorEmail}, #{operatorName}, #{operatorEmail})")
    int addAppGroup(AppGroupDO appGroupDO);

    /**
     * 更新应用组
     *
     * @param appGroupName
     * @param newAppGroupName
     * @param operatorName
     * @return
     */
    @Update("update app_group set app_group_name = #{newAppGroupName}, operator_name = #{operatorName}, "
            + "operator_email = #{operatorEmail} where app_group_name = #{appGroupName}")
    int updateAppGroup(@Param("appGroupName") String appGroupName, @Param("newAppGroupName") String newAppGroupName,
                       @Param("operatorName") String operatorName, @Param("operatorEmail") String operatorEmail);

    /**
     * 删除应用组
     *
     * @param appGroupName
     * @return
     */
    @Update("delete from app_group where app_group_name = #{appGroupName}")
    int deleteAppGroup(@Param("appGroupName") String appGroupName);

    /**
     * 查询所有应用组
     *
     * @return
     */
    @Select("select * from app_group order by modify_time desc ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "operatorName", column = "operator_name"),
            @Result(property = "modifiedTime", column = "modify_time"),
            @Result(property = "createTime", column = "create_time")
    })
    List<AppGroupDO> queryAllAppGroup();

    /**
     * 查询所有应用组
     *
     * @return
     */
    @Select("<script> select * from app_group " +
            "<if test=\"appGroupName != null and appGroupName.length > 0 \"> where app_group_name like concat('%', "
            + "#{appGroupName}, '%') </if>" +
            " order by modify_time desc " +
            " limit #{start}, #{size} </script> ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "operatorName", column = "operator_name"),
            @Result(property = "operatorEmail", column = "operator_email"),
            @Result(property = "creatorName", column = "creator_name"),
            @Result(property = "creatorEmail", column = "creator_email"),
            @Result(property = "modifiedTime", column = "modify_time"),
            @Result(property = "createTime", column = "create_time")
    })
    List<AppGroupDO> queryAppGroupByPage(@Param("appGroupName") String appGroupName, @Param("start") Integer start,
                                         @Param("size") Integer size);

    /**
     * 通过应用组名称来查询
     *
     * @param appGroupName
     * @return
     */
    @Select("select * from app_group where app_group_name = #{appGroupName} order by modify_time desc")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "operatorName", column = "operator_name"),
            @Result(property = "createTime", column = "gmt_create"),
            @Result(property = "modifiedTime", column = "gmt_modify")
    })
    AppGroupDO queryByGroupName(String appGroupName);
}
